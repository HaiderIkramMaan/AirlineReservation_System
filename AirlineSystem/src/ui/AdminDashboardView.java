package ui;

import flight.Airline;
import flight.Airport;
import flight.Flight;
import flight.FlightRegistry;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.Admin;
import report.OccupancyReport;
import report.Report;
import report.RevenueReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class AdminDashboardView extends BorderPane {
    private final Stage stage;
    private final Admin admin;
    private final TableView<Flight> flightTable = new TableView<>();
    private final TextArea reportArea = new TextArea();
    private final TextField flightIdField = new TextField("F950");
    private final TextField flightNumberField = new TextField("AC950");
    private final TextField sourceField = new TextField("KHI");
    private final TextField destinationField = new TextField("LON");
    private final TextField departureField = new TextField("2026-08-25T09:30");
    private final TextField arrivalField = new TextField("2026-08-25T13:45");
    private final TextField priceField = new TextField("620");

    public AdminDashboardView(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        build();
    }

    private void build() {
        setPadding(new Insets(16));

        Label title = new Label("Admin Panel");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button revenueButton = new Button("Generate Revenue Report");
        revenueButton.setOnAction(e -> generateReport("Revenue"));
        Button occupancyButton = new Button("Generate Occupancy Report");
        occupancyButton.setOnAction(e -> generateReport("Occupancy"));
        Button flightsButton = new Button("Refresh Flights");
        flightsButton.setOnAction(e -> refreshFlights());
        Button addDemoButton = new Button("Add Demo Flights");
        addDemoButton.setOnAction(e -> addDemoFlights());
        Button addFlightButton = new Button("Add Flight");
        addFlightButton.setOnAction(e -> addFlightFromForm());
        Button removeFlightButton = new Button("Remove Selected Flight");
        removeFlightButton.setOnAction(e -> removeSelectedFlight());
        HBox flightActions = new HBox(8, addFlightButton, removeFlightButton);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 500, 280)));

        VBox form = new VBox(8);
        form.getChildren().addAll(
                new Label("Add flight"),
                new Label("Flight ID"), flightIdField,
                new Label("Flight Number"), flightNumberField,
                new Label("Source"), sourceField,
                new Label("Destination"), destinationField,
                new Label("Departure (yyyy-MM-ddTHH:mm)"), departureField,
                new Label("Arrival (yyyy-MM-ddTHH:mm)"), arrivalField,
                new Label("Base Price"), priceField
        );

        VBox controls = new VBox(10);
        controls.setPrefWidth(260);
        controls.getChildren().addAll(title, revenueButton, occupancyButton, flightsButton, addDemoButton, flightActions, form, logoutButton);

        // Ensure logout stays visible even when the left panel is compressed.
        controls.setFillWidth(true);

        TableColumn<Flight, String> flightIdCol = new TableColumn<>("Flight ID");
        flightIdCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getFlightId()));
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight");
        flightNumberCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getFlightNumber()));
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getStatus()));
        TableColumn<Flight, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getOrigin().getIataCode() + " -> " + item.getValue().getDestination().getIataCode()));
        TableColumn<Flight, String> departureCol = new TableColumn<>("Departure");
        departureCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getDepartureTime().toString()));
        TableColumn<Flight, String> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getArrivalTime().toString()));
        flightTable.getColumns().addAll(flightIdCol, flightNumberCol, routeCol, departureCol, arrivalCol, statusCol);
        flightTable.setPrefWidth(960);
        flightTable.setPrefHeight(280);

        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefRowCount(10);

        setLeft(controls);
        setCenter(flightTable);
        setBottom(new VBox(8, new Label("Admin Report Output"), reportArea));

        refreshFlights();
    }

    private void generateReport(String type) {
        Report report;
        if ("Revenue".equalsIgnoreCase(type)) {
            report = new RevenueReport(admin, null);
            report.generate();
            reportArea.setText(
                    "Revenue report generated on " + report.getGeneratedDate() + "\n" +
                            "Report ID: " + report.getReportId() + "\n" +
                            "Total Revenue: $" + String.format("%.2f", ((RevenueReport) report).getTotalRevenue())
            );
        } else if ("Occupancy".equalsIgnoreCase(type)) {
            Flight selected = flightTable.getSelectionModel().getSelectedItem();
            report = new OccupancyReport(admin, selected);
            report.generate();
            reportArea.setText("Occupancy rate: " + ((OccupancyReport) report).getOccupancyRate() * 100 + "%");
        } else {
            reportArea.setText("Unsupported report type: " + type);
            return;
        }

        showAlert("Report Created", "Report type: " + type + "");
    }

    private void refreshFlights() {
        List<Flight> flights = FlightRegistry.getInstance().getAllFlights();
        flightTable.getItems().setAll(flights);
        if (!flights.isEmpty()) flightTable.getSelectionModel().select(0);
    }

    private void addDemoFlights() {
        reportArea.setText("Demo flight dataset is available in the flight registry.\n" +
                "The app already contains multiple flights on several routes and dates.");
        refreshFlights();
        showAlert("Demo Data", "Multiple sample flights have been loaded into the registry.");
    }

    private void removeSelectedFlight() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert("Remove Flight", "Select a flight in the table first.");
            return;
        }

        boolean removed = admin.removeFlight(selectedFlight.getFlightId());
        if (removed) {
            refreshFlights();
            reportArea.setText("Removed flight: " + selectedFlight.getFlightNumber());
            showAlert("Flight Removed", "Flight " + selectedFlight.getFlightNumber() + " was removed.");
        } else {
            showAlert("Remove Failed", "Could not remove the selected flight.");
        }
    }

    private void addFlightFromForm() {
        try {
            String flightId = flightIdField.getText() == null ? "" : flightIdField.getText().trim();
            String flightNumber = flightNumberField.getText() == null ? "" : flightNumberField.getText().trim();
            String source = sourceField.getText() == null ? "" : sourceField.getText().trim();
            String destination = destinationField.getText() == null ? "" : destinationField.getText().trim();
            String departureText = departureField.getText() == null ? "" : departureField.getText().trim();
            String arrivalText = arrivalField.getText() == null ? "" : arrivalField.getText().trim();
            String priceText = priceField.getText() == null ? "" : priceField.getText().trim();

            if (flightId.isEmpty() || flightNumber.isEmpty() || source.isEmpty() || destination.isEmpty() || departureText.isEmpty() || arrivalText.isEmpty()) {
                showAlert("Missing Data", "Please fill in all fields before creating a flight.");
                return;
            }

            LocalDateTime departureTime = LocalDateTime.parse(departureText);
            LocalDateTime arrivalTime = LocalDateTime.parse(arrivalText);
            double basePrice = priceText.isEmpty() ? 500.0 : Double.parseDouble(priceText);

            Airport origin = buildAirport(source, true);
            Airport destinationAirport = buildAirport(destination, false);
            Airline airline = new Airline("ALX", "Airline Admin", "AX");

            boolean added = admin.addFlight(flightId, flightNumber, departureTime, arrivalTime, basePrice, airline, origin, destinationAirport);
            if (added) {
                List<Flight> allFlights = FlightRegistry.getInstance().getAllFlights();
                reportArea.setText("Flight added: " + flightNumber + " from " + origin.getIataCode() + " to " + destinationAirport.getIataCode()
                        + " departing " + departureTime + ".\nTotal flights now: " + allFlights.size());
                refreshFlights();
                showAlert("Flight Created", "Flight " + flightNumber + " has been added successfully.");
            } else {
                showAlert("Flight Error", "Unable to add flight. Check the values and try again.");
            }
        } catch (Exception ex) {
            showAlert("Invalid Input", "Use valid date-time values like 2026-08-25T09:30 and a numeric price.");
        }
    }

    private Airport buildAirport(String value, boolean isOrigin) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            return new Airport("APGEN", isOrigin ? "Origin City" : "Destination City", isOrigin ? "Origin" : "Destination", isOrigin ? "ORG" : "DST");
        }

        String upper = normalized.toUpperCase(Locale.ROOT);
        if ("KHI".equals(upper) || "KARACHI".equals(upper)) {
            return new Airport("AP7", "Jinnah International", "Karachi", "KHI");
        }
        if ("LON".equals(upper) || "LONDON".equals(upper)) {
            return new Airport("AP2", "Heathrow", "London", "LON");
        }
        if ("DXB".equals(upper) || "DUBAI".equals(upper)) {
            return new Airport("AP6", "Dubai International", "Dubai", "DXB");
        }
        if ("NYC".equals(upper) || "NEW YORK".equals(upper)) {
            return new Airport("AP1", "John F Kennedy", "New York", "NYC");
        }
        if ("DOH".equals(upper) || "QATAR".equals(upper) || "DOHA".equals(upper)) {
            return new Airport("AP9", "Hamad International", "Doha", "DOH");
        }
        if ("ISB".equals(upper) || "ISLAMABAD".equals(upper)) {
            return new Airport("AP8", "Islamabad International", "Islamabad", "ISB");
        }
        if ("FRA".equals(upper) || "FRANKFURT".equals(upper)) {
            return new Airport("AP10", "Frankfurt Airport", "Frankfurt", "FRA");
        }
        if ("DUB".equals(upper) || "DUBLIN".equals(upper)) {
            return new Airport("AP3", "Dublin Airport", "Dublin", "DUB");
        }
        if ("LAX".equals(upper) || "LOS ANGELES".equals(upper)) {
            return new Airport("AP4", "Los Angeles", "Los Angeles", "LAX");
        }
        if ("SFO".equals(upper) || "SAN FRANCISCO".equals(upper)) {
            return new Airport("AP5", "San Francisco", "San Francisco", "SFO");
        }

        return new Airport("APGEN" + upper, normalized + " Airport", normalized, upper);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
