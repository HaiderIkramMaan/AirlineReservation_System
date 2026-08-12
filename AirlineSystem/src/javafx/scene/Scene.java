package javafx.scene;

public class Scene {
    private Parent root;

    public Scene(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        return root;
    }
}
