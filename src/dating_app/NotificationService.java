package dating_app;

import java.util.HashMap;
import java.util.Map;

public class NotificationService {
    private static final NotificationService INSTANCE = new NotificationService();
    private final Map<String, NotificationObserver> observers = new HashMap<>();

    private NotificationService() {}
    public static NotificationService getInstance() { return INSTANCE; }
    public void add(String userId, NotificationObserver observer) { observers.put(userId, observer); }
    public void remove(String userId) { observers.remove(userId); }

    public void notifyUser(String userId, String message) {
        NotificationObserver observer = observers.get(userId);
        if (observer != null) observer.update(message);
    }

    public void notifyAllUsers(String message) {
        for (NotificationObserver observer : observers.values()) observer.update(message);
    }
}
