package notification_system.entities.notification;

public abstract class NotificationDecorator implements Notification {
    protected Notification notification;

    public NotificationDecorator(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return this.notification;
    }
}
