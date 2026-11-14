package dating_app.strategy;

import dating_app.entities.User;

public interface Matcher {
    double calculateMatchScore(User user, User candidate);
}

