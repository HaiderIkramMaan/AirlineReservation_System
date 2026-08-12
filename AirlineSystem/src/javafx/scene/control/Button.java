package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Button extends javafx.scene.Parent {
    private String label;
    private boolean disabled;
    private EventHandler<ActionEvent> handler;

    public Button(String label) { this.label = label; }

    public void setDisable(boolean disabled) { this.disabled = disabled; }
    public boolean isDisabled() { return disabled; }

    public void setOnAction(EventHandler<ActionEvent> handler) { this.handler = handler; }
    public void fire() { if (handler != null) handler.handle(new ActionEvent()); }
    public String getText() { return label; }
}
