package dating_app.observer;

import dating_app.entities.Message;

public class UserNotificationObserver extends NotificationObserver {
    private final String userId;

    public UserNotificationObserver(String userId) { this.userId = userId; }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + userId + ": " + message);
    }
}

