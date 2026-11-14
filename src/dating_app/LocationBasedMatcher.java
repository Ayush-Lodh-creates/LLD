package dating_app;

public class LocationBasedMatcher implements Matcher {
    @Override
    public double calculateMatchScore(User user, User candidate) {
        double distance = user.getProfile().getLocation().distanceInKm(candidate.getProfile().getLocation());
        return 1.0 / (1.0 + distance); // Closer users have higher scores.
    }
}
