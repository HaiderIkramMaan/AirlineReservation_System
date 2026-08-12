package ui;

import booking.Booking;
import flight.Flight;
import flight.FlightRegistry;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import payment.CreditCardPayment;
import person.Passenger;
import seat.Seat;

import java.time.LocalDate;
import java.util.List;

public class PassengerDashboardView extends BorderPane {
    private final Stage stage;
    private final Passenger passenger;
    private final TableView<Flight> flightTable = new TableView<>();
    private final TableView<Booking> bookingTable = new TableView<>();
    private final ListView<String> seatList = new ListView<>();
    private final TextField sourceField = new TextField();
    private final TextField destinationField = new TextField();
    private final DatePicker datePicker = new DatePicker(LocalDate.now());

    public PassengerDashboardView(Stage stage, Passenger passenger) {
        this.stage = stage;
        this.passenger = passenger;
        build();
        refreshBookings();
    }

    private void build() {
        setPadding(new Insets(16));

        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(0, 0, 12, 0));
        sourceField.setPromptText("Source (optional)");
        destinationField.setPromptText("Destination (optional)");
        sourceField.setPrefWidth(120);
        destinationField.setPrefWidth(120);
        datePicker.setPrefWidth(150);
        Button searchButton = new Button("Search Flights");
        searchButton.setOnAction(e -> searchFlights());
        Button showAllButton = new Button("Show All");
        showAllButton.setOnAction(e -> {
            sourceField.clear();
            destinationField.clear();
            searchFlights();
        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 480, 260)));
        searchBox.getChildren().addAll(new Label("From:"), sourceField, new Label("To:"), destinationField, new Label("Date:"), datePicker, searchButton, showAllButton, logoutButton);

        TableColumn<Flight, String> flightNoCol = new TableColumn<>("Flight");
        flightNoCol.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFlightNumber()));
        TableColumn<Flight, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigin().getIataCode() + " -> " + item.getValue().getDestination().getIataCode()));
        TableColumn<Flight, String> departCol = new TableColumn<>("Departure");
        departCol.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDepartureTime().toString()));
        flightTable.getColumns().addAll(flightNoCol, routeCol, departCol);
        flightTable.setPrefHeight(240);
        flightTable.setPrefWidth(560);
        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) fillSeatChoices(newValue);
        });

        Button bookButton = new Button("Book Selected Seat");
        bookButton.setOnAction(e -> bookSelectedSeat());

        VBox right = new VBox(10);
        right.setPadding(new Insets(0, 0, 0, 12));
        right.setPrefWidth(260);
        right.getChildren().addAll(new Label("Available Seats"), seatList, bookButton);

        HBox center = new HBox(12);
        center.getChildren().addAll(flightTable, right);

        bookingTable.setPrefHeight(180);
        TableColumn<Booking, String> bookingIdCol = new TableColumn<>("Booking");
        bookingIdCol.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getBookingId()));
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getStatus().toString()));
        TableColumn<Booking, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(item -> new SimpleStringProperty(String.format("$%.2f", item.getValue().getTotalPrice())));
        bookingTable.getColumns().addAll(bookingIdCol, statusCol, totalCol);

        VBox bottom = new VBox(8);
        bottom.setPadding(new Insets(12, 0, 0, 0));
        bottom.getChildren().addAll(new Label("Booking History"), bookingTable);

        setTop(searchBox);
        setCenter(center);
        setBottom(bottom);

        searchFlights();
    }

    private void searchFlights() {
        String source = sourceField.getText() == null ? "" : sourceField.getText().trim();
        String destination = destinationField.getText() == null ? "" : destinationField.getText().trim();
        LocalDate date = datePicker.getValue();
        List<Flight> flights = passenger.searchFlights(source, destination, date);
        flightTable.getItems().setAll(flights);
        if (!flights.isEmpty()) {
            flightTable.getSelectionModel().select(0);
        }
    }

    private void fillSeatChoices(Flight flight) {
        seatList.getItems().clear();
        if (flight == null) return;
        List<Seat> availableSeatList = flight.getAvailableSeats();
        if (availableSeatList.isEmpty()) {
            seatList.getItems().add("No seats available");
            return;
        }
        for (Seat seat : availableSeatList) {
            seatList.getItems().add(seat.getSeatId() + " - " + seat.getTravelClass() + " ($" + String.format("%.2f", seat.getPrice(flight.getBasePrice())) + ")");
        }
    }

    private void bookSelectedSeat() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert("Booking Error", "Select a flight first.");
            return;
        }

        String selectedSeatText = seatList.getSelectionModel().getSelectedItem();
        if (selectedSeatText == null) {
            showAlert("Booking Error", "Choose a seat to book.");
            return;
        }

        String seatId = selectedSeatText.split(" - ")[0];
        Seat selectedSeat = selectedFlight.getAvailableSeats().stream()
                .filter(s -> s.getSeatId().equals(seatId))
                .findFirst()
                .orElse(null);

        if (selectedSeat == null) {
            showAlert("Booking Error", "Seat not available.");
            return;
        }

        CreditCardPayment payment = new CreditCardPayment(selectedFlight.getBasePrice(), "4111111111111111", passenger.getName(), "12/30", "123");
        Booking booking = passenger.bookFlight(selectedFlight, selectedSeat, payment);
        if (booking != null) {
            showAlert("Booked", "Booking created: " + booking.getBookingId());
            refreshBookings();
            searchFlights();
        } else {
            showAlert("Booking Failed", "Booking could not be created.");
        }
    }

    private void refreshBookings() {
        bookingTable.getItems().setAll(passenger.viewBookingHistory());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
