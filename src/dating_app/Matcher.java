package dating_app;

public interface Matcher {
    double calculateMatchScore(User user, User candidate);
}
