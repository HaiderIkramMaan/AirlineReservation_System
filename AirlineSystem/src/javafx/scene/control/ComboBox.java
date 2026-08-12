package javafx.scene.control;

import java.util.ArrayList;
import java.util.List;

public class ComboBox<T> extends javafx.scene.Parent {
    private final Items items = new Items();
    private T value;

    public Items getItems() { return items; }

    public class Items {
        private final List<T> backing = new ArrayList<>();
        public void setAll(@SuppressWarnings("rawtypes") java.util.Collection c) {
            backing.clear();
            if (c != null) {
                for (Object o : c) {
                    try { @SuppressWarnings("unchecked") T t = (T)o; backing.add(t); } catch (Exception ignored) {}
                }
            }
        }
        @SafeVarargs
        public final void setAll(T... itemsArr) {
            backing.clear();
            if (itemsArr != null) {
                for (T t : itemsArr) {
                    backing.add(t);
                }
            }
        }
        public List<T> toList() { return new ArrayList<>(backing); }
    }

    public void setValue(T value) { this.value = value; }
    public T getValue() { return value; }
}
