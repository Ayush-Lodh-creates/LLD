package notification_system.entities.subscriber;

import notification_system.entities.notification.Notification;

public interface Subscriber {

    void onEvent(Notification notification);
}
