package notification_system.entities.delivery_strategy;

public interface DeliveryStrategy {

    void sendNotification(String message, String clientId, String severity);
}
