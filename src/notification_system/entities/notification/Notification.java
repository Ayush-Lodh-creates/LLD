package notification_system.entities.notification;

import notification_system.enums.Severity;

public interface Notification {

    public String getContent();
    public String getClientId();
    public Severity getSeverity();
}
