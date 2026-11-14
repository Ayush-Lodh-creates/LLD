package dating_app;

public class UserNotificationObserver extends NotificationObserver {
    private final String userId;

    public UserNotificationObserver(String userId) { this.userId = userId; }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + userId + ": " + message);
    }
}
