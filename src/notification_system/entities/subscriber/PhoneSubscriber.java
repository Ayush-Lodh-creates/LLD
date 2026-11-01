package notification_system.entities.subscriber;

import notification_system.entities.DeliveryFactory;
import notification_system.entities.delivery_strategy.DeliveryStrategy;
import notification_system.entities.notification.Notification;

public class PhoneSubscriber implements Subscriber {

    private Long phoneNumber;

    public PhoneSubscriber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onEvent(Notification notification) {
        // Logic to send phone notification
        DeliveryStrategy deliveryStrategy = DeliveryFactory.getStrategy(notification.getSeverity());
        deliveryStrategy.sendNotification("Sending call to client phone " + phoneNumber + " with message : " + notification.getContent(),
                notification.getClientId(), notification.getSeverity().name());
    }
}
