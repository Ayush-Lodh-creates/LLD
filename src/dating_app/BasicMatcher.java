package dating_app;

public class BasicMatcher implements Matcher {
    @Override
    public double calculateMatchScore(User user, User candidate) {
        return user.getPreference().meets(candidate.getProfile())
                && candidate.getPreference().meets(user.getProfile()) ? 1 : 0;
    }
}
