package dating_app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DatingApp {
    private final List<User> users = new ArrayList<>();
    private final List<ChatRoom> chatRooms = new ArrayList<>();
    private Matcher matcher = MatcherFactory.createMatcher(MatcherType.BASIC);

    public void addUser(User user) {
        for (User existing : users) {
            if (existing.getUserId().equals(user.getUserId())) throw new IllegalArgumentException("Duplicate user ID");
        }
        users.add(user);
        NotificationService.getInstance().add(user.getUserId(), user.getNotificationObserver());
    }

    public User getUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) return user;
        }
        throw new IllegalArgumentException("Unknown user: " + userId);
    }

    public void setMatcher(MatcherType type) { matcher = MatcherFactory.createMatcher(type); }
    public List<ChatRoom> getChatRooms() { return List.copyOf(chatRooms); }

    public List<User> getCandidates(String userId) {
        User user = getUser(userId);
        List<User> candidates = new ArrayList<>();
        List<User> nearby = LocationService.getInstance().findNearbyUsers(
                user.getProfile().getLocation(), user.getPreference().getMaxDistance(), users);
        for (User candidate : nearby) {
            if (userId.equals(candidate.getUserId()) || user.hasInteractedWith(candidate.getUserId())) continue;
            double distance = user.getProfile().getLocation().distanceInKm(candidate.getProfile().getLocation());
            if (user.getPreference().meets(candidate.getProfile())
                    && candidate.getPreference().meets(user.getProfile())
                    && distance <= candidate.getPreference().getMaxDistance()) {
                candidates.add(candidate);
            }
        }
        candidates.sort(Comparator.comparingDouble((User candidate) -> matcher.calculateMatchScore(user, candidate)).reversed());
        return candidates;
    }

    // A match requires two right swipes. Repeated swipes reuse the same room.
    // Changing a swipe does not remove an existing match in this simple demo.
    public ChatRoom swipe(String userId, String targetId, SwipeAction action) {
        User user = getUser(userId);
        User target = getUser(targetId);
        user.swipe(targetId, action);
        if (action != SwipeAction.RIGHT || !target.hasLiked(userId)) return null;
        for (ChatRoom room : chatRooms) {
            if (room.getParticipantIds().contains(userId) && room.getParticipantIds().contains(targetId)) return room;
        }
        ChatRoom room = new ChatRoom(userId, targetId);
        chatRooms.add(room);
        NotificationService notifications = NotificationService.getInstance();
        notifications.notifyUser(userId, "You matched with " + target.getProfile().getName());
        notifications.notifyUser(targetId, "You matched with " + user.getProfile().getName());
        return room;
    }
}
