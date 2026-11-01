package notification_system.entities.notification;

import notification_system.enums.Severity;

public class HeaderDecorator extends NotificationDecorator {

    private String header;

    public HeaderDecorator(Notification notification, String header) {
        super(notification);
        this.header = header;
    }

    @Override
    public String getContent() {
        return super.getNotification().getContent() + "\nHeader: " + header;
    }

    @Override
    public String getClientId() {
        return super.getNotification().getClientId();
    }

    @Override
    public Severity getSeverity() {
        return super.getNotification().getSeverity();
    }
}
