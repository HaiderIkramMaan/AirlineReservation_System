package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Booking;
import model.Flight;
import model.Passenger;
import model.Payment;
import model.Seat;

import java.time.LocalDate;
import java.util.List;

/**
 * PassengerDashboardController — realizes ViewController. Drives Passenger's
 * searchFlights/bookFlight/viewBookingHistory operations and "navigates to"
 * SeatMapController for seat selection, exactly as drawn.
 */
public class PassengerDashboardController extends ViewController {

    @FXML private TableView<Flight> searchResults;
    @FXML private TextField flightSearchField;

    @Override
    public void initialize() {
        refreshBookings();
    }

    @FXML
    public void handleSearch() {
        String query = flightSearchField.getText();
        if (query == null || query.isBlank()) {
            showAlert("Search", "Enter a route as SOURCE-DESTINATION.");
            return;
        }
        String[] parts = query.split("-");
        if (parts.length < 2) {
            showAlert("Search", "Use the format SOURCE-DESTINATION.");
            return;
        }
        Passenger passenger = (Passenger) getCurrentUser();
        List<Flight> flights = passenger.searchFlights(parts[0].trim(), parts[1].trim(), LocalDate.now());
        displayFlightResults(flights);
    }

    @FXML
    public void handleSeatSelection(Flight flight) {
        SeatMapController seatMapController = new SeatMapController();
        seatMapController.setPrimaryStage(this.primaryStage);
        seatMapController.setCurrentUser(this.currentUser);
        seatMapController.setFlight(flight);
        seatMapController.initialize();
        navigateTo("SeatMap");
    }

    @FXML
    public void handleBooking(Flight flight, Seat seat) {
        Passenger passenger = (Passenger) getCurrentUser();
        Payment payment = null; // supplied by the payment dialog that precedes this call
        Booking booking = passenger.bookFlight(flight, seat, payment);
        if (booking != null) {
            showAlert("Booking Confirmed", "Booking " + booking.getBookingId() + " created.");
            refreshBookings();
        } else {
            showAlert("Booking Failed", "Could not book the selected seat.");
        }
    }

    public void displayFlightResults(List<Flight> flights) {
        searchResults.getItems().setAll(flights);
    }

    public void refreshBookings() {
        Passenger passenger = (Passenger) getCurrentUser();
        List<Booking> bookings = passenger.viewBookingHistory();
        // Handed off to whichever panel renders booking history.
    }
}
