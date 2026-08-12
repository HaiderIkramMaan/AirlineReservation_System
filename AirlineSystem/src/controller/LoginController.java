package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import person.Admin;
import person.Passenger;
import person.Person;
import service.AuthenticationService;


/**
 * LoginController — realizes ViewController, matches the "authenticates"
 * association pointing at Person.
 */
public class LoginController extends ViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;


    @Override
    public void initialize() {
        // FXML fields are injected by the loader; nothing else to prep.
    }

    @FXML
    public void handleLogin() {
        if (!validateInput()) {
            showAlert("Login Failed", "Please enter both a username and a password.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        Person authenticated = AuthenticationService.getInstance().authenticateByEmail(username, password);

        if (authenticated == null) {
            showAlert("Login Failed", "Invalid credentials. Please try again.");
            return;
        }

        setCurrentUser(authenticated);

        if (authenticated instanceof Admin) {
            navigateTo("AdminDashboard");
        } else if (authenticated instanceof Passenger) {
            navigateTo("PassengerDashboard");
        }
    }

    @FXML
    public void handleRegister() {
        navigateTo("Register");
    }

    public boolean validateInput() {
        return usernameField.getText() != null && !usernameField.getText().trim().isEmpty()
                && passwordField.getText() != null && !passwordField.getText().isEmpty();
    }

}
