package dating_app.entities;

import dating_app.enums.SwipeAction;
import dating_app.observer.NotificationObserver;
import dating_app.observer.UserNotificationObserver;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final String userId;
    private final UserProfile profile;
    private final Preference preference;
    private final Map<String, SwipeAction> swipeHistory = new HashMap<>();
    private final NotificationObserver notificationObserver;

    public User(String userId, UserProfile profile, Preference preference) {
        this.userId = userId;
        this.profile = profile;
        this.preference = preference;
        this.notificationObserver = new UserNotificationObserver(userId);
    }

    public String getUserId() { return userId; }
    public UserProfile getProfile() { return profile; }
    public Preference getPreference() { return preference; }
    public NotificationObserver getNotificationObserver() { return notificationObserver; }
    public boolean hasLiked(String otherId) { return swipeHistory.get(otherId) == SwipeAction.RIGHT; }
    public boolean hasDisliked(String otherId) { return swipeHistory.get(otherId) == SwipeAction.LEFT; }
    public boolean hasInteractedWith(String otherId) { return swipeHistory.containsKey(otherId); }

    public void swipe(String otherId, SwipeAction action) {
        if (userId.equals(otherId)) throw new IllegalArgumentException("Cannot swipe on yourself");
        if (action == null) throw new IllegalArgumentException("Swipe action is required");
        swipeHistory.put(otherId, action);
    }
}

