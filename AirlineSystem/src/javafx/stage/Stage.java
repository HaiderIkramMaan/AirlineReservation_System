package javafx.stage;

import javafx.scene.Scene;

public class Stage {
    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void show() {
        System.out.println("[Stage] show()");
    }
}
