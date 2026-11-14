package dating_app.strategy;

import dating_app.entities.User;

public class BasicMatcher implements Matcher {
    @Override
    public double calculateMatchScore(User user, User candidate) {
        return user.getPreference().meets(candidate.getProfile())
                && candidate.getPreference().meets(user.getProfile()) ? 1 : 0;
    }
}

