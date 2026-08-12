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
import person.Passenger;
import service.AuthenticationService;

public class RegisterView extends VBox {
    private final Stage stage;
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField phoneField = new TextField();
    private final TextField passportField = new TextField();
    private final TextField visaField = new TextField();

    public RegisterView(Stage stage) {
        this.stage = stage;
        build();
    }

    private void build() {
        setPadding(new Insets(20));
        setSpacing(12);
        setAlignment(Pos.CENTER);

        Label title = new Label("Passenger Registration");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(new Label("Full Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(emailField, 1, 1);
        form.add(new Label("Password:"), 0, 2);
        form.add(passwordField, 1, 2);
        form.add(new Label("Phone:"), 0, 3);
        form.add(phoneField, 1, 3);
        form.add(new Label("Passport:"), 0, 4);
        form.add(passportField, 1, 4);
        form.add(new Label("Visa:"), 0, 5);
        form.add(visaField, 1, 5);

        Button submit = new Button("Create Account");
        submit.setOnAction(e -> register());
        Button back = new Button("Back to Login");
        back.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 480, 260)));

        getChildren().addAll(title, form, submit, back);
    }

    private void register() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();
        String passport = passportField.getText() == null ? "" : passportField.getText().trim();
        String visa = visaField.getText() == null ? "" : visaField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Name, email, and password are required.");
            return;
        }

        String id = "P" + (int)(Math.random() * 9000 + 1000);
        Passenger passenger = new Passenger(id, name, email, password, phone, passport, visa);
        AuthenticationService.getInstance().registerUser(passenger);
        showAlert("Registration Successful", "Account created for " + email + ". You can now log in.");
        stage.setScene(new Scene(new LoginView(stage), 480, 260));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
