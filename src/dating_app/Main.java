package dating_app;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatingApp app = new DatingApp();
        UserProfile arjun = new UserProfile("Arjun", 25, Gender.MALE, "Music and travel", new Location(12.9716, 77.5946));
        arjun.addInterest(new Interest("Music", "Art"));
        UserProfile priya = new UserProfile("Priya", 24, Gender.FEMALE, "Live music enthusiast", new Location(12.9750, 77.6000));
        priya.addInterest(new Interest("Music", "Art"));
        app.addUser(new User("u1", arjun, new Preference(22, 30, 20, List.of("Music"), List.of(Gender.FEMALE))));
        app.addUser(new User("u2", priya, new Preference(23, 30, 20, List.of("Music"), List.of(Gender.MALE))));

        app.setMatcher(MatcherType.INTEREST);
        System.out.println("Candidates for Arjun:");
        for (User candidate : app.getCandidates("u1")) candidate.getProfile().display();

        app.swipe("u1", "u2", SwipeAction.RIGHT);
        ChatRoom room = app.swipe("u2", "u1", SwipeAction.RIGHT);
        if (room != null) {
            room.addMessage("u1", "Hi Priya! What music do you like?");
            room.addMessage("u2", "Hi Arjun! Mostly indie music.");
            room.displayChat();
        }
    }
}
