package ui;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.Admin;
import report.OccupancyReport;
import report.Report;
import report.RevenueReport;

import java.util.List;

public class AdminDashboardView extends BorderPane {
    private final Stage stage;
    private final Admin admin;
    private final TableView<Flight> flightTable = new TableView<>();
    private final TextArea reportArea = new TextArea();

    public AdminDashboardView(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        build();
    }

    private void build() {
        setPadding(new Insets(16));

        Button revenueButton = new Button("Generate Revenue Report");
        revenueButton.setOnAction(e -> generateReport("Revenue"));
        Button occupancyButton = new Button("Generate Occupancy Report");
        occupancyButton.setOnAction(e -> generateReport("Occupancy"));
        Button flightsButton = new Button("Refresh Flights");
        flightsButton.setOnAction(e -> refreshFlights());
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 480, 260)));

        VBox controls = new VBox(10);
        controls.getChildren().addAll(revenueButton, occupancyButton, flightsButton, logoutButton);

        TableColumn<Flight, String> flightIdCol = new TableColumn<>("Flight");
        flightIdCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getFlightNumber()));
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getStatus()));
        TableColumn<Flight, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(item -> new javafx.beans.property.SimpleStringProperty(item.getValue().getOrigin().getIataCode() + " -> " + item.getValue().getDestination().getIataCode()));
        flightTable.getColumns().addAll(flightIdCol, routeCol, statusCol);
        flightTable.setPrefWidth(700);
        flightTable.setPrefHeight(280);

        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefRowCount(10);

        setLeft(controls);
        setCenter(flightTable);
        setBottom(new VBox(8, new Label("Report Output"), reportArea));

        refreshFlights();
    }

    private void generateReport(String type) {
        Report report;
        if ("Revenue".equalsIgnoreCase(type)) {
            report = new RevenueReport(admin, null);
            report.generate();
            reportArea.setText("Revenue report generated on " + report.getGeneratedDate() + "\n" + "Report ID: " + report.getReportId());
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
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
