package javafx.scene.control;

public class Alert {
    public enum AlertType { INFORMATION }

    private AlertType type;
    private String title;
    private String headerText;
    private String contentText;

    public Alert(AlertType type) {
        this.type = type;
    }

    public void setTitle(String title) { this.title = title; }
    public void setHeaderText(String headerText) { this.headerText = headerText; }
    public void setContentText(String contentText) { this.contentText = contentText; }
    public void showAndWait() { System.out.println("[Alert] " + title + ": " + contentText); }
}
