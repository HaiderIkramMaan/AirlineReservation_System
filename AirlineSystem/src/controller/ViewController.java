package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;

/**
 * «abstract» ViewController
 * Base class for every screen controller in the app.
 * Fields/methods mirror the UML exactly: currentUser, primaryStage,
 * initialize(), handleLogout(), navigateTo(view), showAlert(title,msg),
 * getCurrentUser().
 */
public abstract class ViewController {

    protected Person currentUser;
    protected Stage primaryStage;

    /** Each concrete controller wires up its own FXML-injected controls here. */
    public abstract void initialize();

    public void handleLogout() {
        currentUser = null;
        navigateTo("LoginView");
    }

    public void navigateTo(String view) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + view + ".fxml"));
            Parent root = loader.load();

            // Hand off shared state (window + logged-in user) to the next controller
            Object next = loader.getController();
            if (next instanceof ViewController nextController) {
                nextController.setPrimaryStage(this.primaryStage);
                nextController.setCurrentUser(this.currentUser);
                nextController.initialize();
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load view: " + view);
        }
    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    // --- Not shown on the diagram, but required plumbing so subclasses can
    // share state through navigateTo() without duplicating fields. Kept
    // package/public-minimal so it never collides with the public API above.
    protected void setCurrentUser(Person user) {
        this.currentUser = user;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
