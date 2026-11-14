package dating_app.strategy;

import dating_app.entities.Interest;
import dating_app.entities.User;

public class InterestBasedMatcher implements Matcher {
    @Override
    public double calculateMatchScore(User user, User candidate) {
        int commonInterests = 0;
        for (String wanted : user.getPreference().getInterests()) {
            for (Interest interest : candidate.getProfile().getInterests()) {
                if (wanted.equalsIgnoreCase(interest.getName())) {
                    commonInterests++;
                    break;
                }
            }
        }
        return commonInterests;
    }
}

