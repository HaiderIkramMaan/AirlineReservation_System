package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Admin;
import model.Passenger;
import model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * LoginController — realizes ViewController, matches the "authenticates"
 * association pointing at Person.
 */
public class LoginController extends ViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Populated by the persistence layer (FileManager + DataSerializer<Person>)
    // at startup; kept private since the diagram doesn't expose how accounts
    // are loaded, only that LoginController authenticates a Person.
    private final List<Person> registeredUsers = new ArrayList<>();

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

        Person authenticated = authenticate(usernameField.getText().trim());

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

    // Looks up the account by email, then defers the actual credential check
    // to Person#login() / the protected Person#verifyPassword(), exactly as
    // declared on the diagram (both take no extra parameters there).
    private Person authenticate(String username) {
        for (Person p : registeredUsers) {
            if (p.getEmail().equalsIgnoreCase(username) && p.login()) {
                return p;
            }
        }
        return null;
    }
}
