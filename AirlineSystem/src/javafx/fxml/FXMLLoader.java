package javafx.fxml;

import javafx.scene.Parent;

import java.net.URL;

public class FXMLLoader {
    private final URL url;
    private Object controller;

    public FXMLLoader(URL url) {
        this.url = url;
    }

    public Parent load() throws java.io.IOException {
        // Minimal stub - doesn't actually load FXML.
        return new Parent();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
