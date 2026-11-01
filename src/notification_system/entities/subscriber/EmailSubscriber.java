package notification_system.entities.subscriber;

import notification_system.entities.DeliveryFactory;
import notification_system.entities.delivery_strategy.DeliveryStrategy;
import notification_system.entities.notification.Notification;

public class EmailSubscriber implements Subscriber {

    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void onEvent(Notification notification) {
        // Logic to send email notification
        DeliveryStrategy deliveryStrategy = DeliveryFactory.getStrategy(notification.getSeverity());
        deliveryStrategy.sendNotification("Sending notification to client email " + email + " with message : " + notification.getContent(),
                notification.getClientId(), notification.getSeverity().name());
    }
}
