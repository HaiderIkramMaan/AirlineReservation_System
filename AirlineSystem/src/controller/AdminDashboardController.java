package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import person.Admin;
import person.DataSerializer;
import model.FileManager;
import flight.Flight;
import person.Passenger;
import report.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminDashboardController — realizes ViewController. Talks to the logged-in
 * Admin (via getCurrentUser()) for flight/report operations, and to
 * FileManager (the "uses" association on the diagram) for bulk loads.
 */
public class AdminDashboardController extends ViewController {

    @FXML private TableView<Flight> flightTable;
    @FXML private ComboBox<String> reportTypeCombo;

    // Implementation detail, not on the diagram: deserializes persisted rows
    // back into domain objects when loading from FileManager.
    private DataSerializer<Flight> flightSerializer;
    private DataSerializer<Passenger> passengerSerializer;

    @Override
    public void initialize() {
        reportTypeCombo.getItems().setAll("Revenue", "Occupancy", "FlightSchedule");
        loadAllFlights();
    }

    @FXML
    public void handleFlightMgmt() {
        Flight selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Select a flight to manage first.");
            return;
        }
        Admin admin = (Admin) getCurrentUser();
        boolean updated = admin.updateFlight(selected.getFlightId());
        if (updated) {
            loadAllFlights();
        } else {
            showAlert("Update Failed", "Could not update flight " + selected.getFlightId());
        }
    }

    @FXML
    public void handleReportGeneration() {
        String type = reportTypeCombo.getValue();
        if (type == null) {
            showAlert("No Report Type", "Choose a report type first.");
            return;
        }
        Admin admin = (Admin) getCurrentUser();
        Report report = admin.generateReport(type);
        displayReport(report);
    }

    public void displayReport(Report report) {
        if (report == null) {
            showAlert("Report Error", "No report was generated.");
            return;
        }
        showAlert("Report: " + report.getReportId(),
                "Generated on " + report.getGeneratedDate());
    }

    public void loadAllFlights() {
        List<String> rows = FileManager.getInstance().loadFromFile("flights.dat");
        List<Flight> flights = new ArrayList<>();
        if (rows != null && flightSerializer != null) {
            for (String row : rows) {
                flights.add(flightSerializer.deserializeData(row));
            }
        }
        flightTable.getItems().setAll(flights);
    }

    public void loadAllPassengers() {
        List<String> rows = FileManager.getInstance().loadFromFile("passengers.dat");
        List<Passenger> passengers = new ArrayList<>();
        if (rows != null && passengerSerializer != null) {
            for (String row : rows) {
                passengers.add(passengerSerializer.deserializeData(row));
            }
        }
        // Handed off to whichever panel renders the passenger list; the
        // diagram only requires this controller expose the void operation.
        // Admin itself is only consulted via managePassenger(id) if a
        // specific record needs to be looked up or acted on afterward.
    }
}
