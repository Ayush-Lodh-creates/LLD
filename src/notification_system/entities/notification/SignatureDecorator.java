package notification_system.entities.notification;

import notification_system.enums.Severity;

public class SignatureDecorator extends NotificationDecorator {

    private String signature;

    public SignatureDecorator(Notification notification, String signature) {
        super(notification);
        this.signature = signature;
    }

    @Override
    public String getContent() {
        return super.getNotification().getContent() + "\nSignature: " + signature;
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
