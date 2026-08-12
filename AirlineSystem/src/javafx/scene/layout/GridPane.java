package javafx.scene.layout;

import java.util.ArrayList;
import java.util.List;

public class GridPane extends javafx.scene.Parent {
    private final List<Object> children = new ArrayList<>();

    public List<Object> getChildren() { return children; }

    public void add(Object node, int col, int row) {
        children.add(node);
    }
}
