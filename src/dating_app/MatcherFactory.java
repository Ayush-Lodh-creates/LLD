package dating_app;

public class MatcherFactory {
    public static Matcher createMatcher(MatcherType type) {
        return switch (type) {
            case BASIC -> new BasicMatcher();
            case INTEREST -> new InterestBasedMatcher();
            case LOCATION -> new LocationBasedMatcher();
        };
    }
}
