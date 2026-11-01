package notification_system.entities.notification;

import notification_system.enums.Severity;

public class SimpleNotification implements Notification {

    private String clientId;
    private String content;
    private Severity severity;

    public SimpleNotification(String content, Severity severity, String clientId) {
        this.content = content;
        this.severity = severity;
        this.clientId = clientId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
