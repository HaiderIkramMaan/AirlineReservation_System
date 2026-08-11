package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.Flight;
import model.Seat;

/**
 * SeatMapController — realizes ViewController (curved arrow back to it on
 * the diagram). Owns the flight/selectedSeat/seatGrid state and the
 * render/click/update/select operations shown.
 */
public class SeatMapController extends ViewController {

    private Flight flight;
    private Seat selectedSeat;
    @FXML private GridPane seatGrid;

    @Override
    public void initialize() {
        if (flight != null) {
            renderSeatMap();
        }
    }

    public void renderSeatMap() {
        seatGrid.getChildren().clear();
        for (Seat seat : flight.getAvailableSeats()) {
            Button seatButton = new Button(seat.getSeatNumber());
            seatButton.setDisable(!seat.isAvailable());
            seatButton.setOnAction(e -> handleSeatClick(seat));
            seatGrid.add(seatButton, seat.getColumn(), seat.getRow());
        }
    }

    public void handleSeatClick(Seat seat) {
        if (!seat.isAvailable()) {
            showAlert("Unavailable", "Seat " + seat.getSeatNumber() + " is already taken.");
            return;
        }
        this.selectedSeat = seat;
        updateSeatDisplay();
    }

    public void updateSeatDisplay() {
        // Re-render so the newly selected seat is visually highlighted.
        renderSeatMap();
    }

    public Seat getSelectedSeat() {
        return selectedSeat;
    }

    public void clearSelection() {
        this.selectedSeat = null;
        updateSeatDisplay();
    }

    // Not on the diagram as a public operation, but required so
    // PassengerDashboardController can hand off the chosen flight when it
    // navigates here — matches the "flight: Flight" attribute shown.
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
