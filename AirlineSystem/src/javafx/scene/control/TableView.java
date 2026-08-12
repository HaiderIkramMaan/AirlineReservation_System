package javafx.scene.control;

import java.util.ArrayList;
import java.util.List;

public class TableView<T> extends javafx.scene.Parent {
    private final Items items = new Items();
    private final SelectionModel selectionModel = new SelectionModel();

    public Items getItems() { return items; }
    public SelectionModel getSelectionModel() { return selectionModel; }

    public class Items {
        private final List<T> backing = new ArrayList<>();
        public void setAll(java.util.Collection<? extends T> c) {
            backing.clear();
            if (c != null) backing.addAll(c);
        }
        public List<T> toList() { return new ArrayList<>(backing); }
    }

    public class SelectionModel {
        public T getSelectedItem() { return null; }
    }
}
