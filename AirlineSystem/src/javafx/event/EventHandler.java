package javafx.event;

@FunctionalInterface
public interface EventHandler<T> {
    void handle(T event);
}
