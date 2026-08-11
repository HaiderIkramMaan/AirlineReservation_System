import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private Person recipient;
    private String message;
    private LocalDateTime timestamp;
    private NotificationType type;

    public Notification() {
        this.timestamp = LocalDateTime.now();
    }

    public Notification(String notificationId, Person recipient, String message, NotificationType type) {
        this.notificationId = notificationId;
        this.recipient = recipient;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    public boolean send() {
        if (recipient == null) {
            System.err.println("Cannot send notification: recipient is null.");
            return false;
        }
        System.out.println("----------------------------------------");
        System.out.println("Sending [" + type + "] Notification to " + recipient.getName() + " (" + recipient.getEmail() + "):");
        System.out.println("Message: " + message);
        System.out.println("Time: " + timestamp);
        System.out.println("----------------------------------------");
        return true;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
