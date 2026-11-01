package notification_system.entities.delivery_strategy;

public class HighStrategy implements DeliveryStrategy {
    @Override
    public void sendNotification(String message, String clientId, String severity) {
        System.out.println("Sending " + severity + " severity notification to client " + clientId + ": " + message);
    }
}
