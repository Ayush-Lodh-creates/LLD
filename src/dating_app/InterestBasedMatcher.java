package dating_app;

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
