package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.Admin;
import person.Passenger;
import person.Person;
import service.AuthenticationService;

public class LoginView extends VBox {
    private final Stage stage;
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();

    public LoginView(Stage stage) {
        this.stage = stage;
        build();
    }

    private void build() {
        setPadding(new Insets(24));
        setSpacing(16);
        setAlignment(Pos.CENTER);

        Label title = new Label("Airline Reservation System");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.setPadding(new Insets(10));
        form.add(new Label("Email:"), 0, 0);
        form.add(emailField, 1, 0);
        form.add(new Label("Password:"), 0, 1);
        form.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());

        emailField.setPromptText("john.doe@example.com");
        passwordField.setPromptText("password");

        getChildren().addAll(title, form, loginButton);
    }

    private void handleLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        Person authenticated = AuthenticationService.getInstance().authenticateByEmail(email, password);
        if (authenticated == null) {
            showAlert("Login Failed", "Invalid credentials. Please try again.");
            return;
        }

        if (authenticated instanceof Admin) {
            stage.setScene(new Scene(new AdminDashboardView(stage, (Admin) authenticated), 900, 620));
        } else if (authenticated instanceof Passenger) {
            stage.setScene(new Scene(new PassengerDashboardView(stage, (Passenger) authenticated), 900, 620));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
