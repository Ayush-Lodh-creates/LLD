package dating_app.factory;

import dating_app.entities.Interest;
import dating_app.entities.Location;
import dating_app.enums.MatcherType;
import dating_app.strategy.BasicMatcher;
import dating_app.strategy.InterestBasedMatcher;
import dating_app.strategy.LocationBasedMatcher;
import dating_app.strategy.Matcher;

public class MatcherFactory {
    public static Matcher createMatcher(MatcherType type) {
        return switch (type) {
            case BASIC -> new BasicMatcher();
            case INTEREST -> new InterestBasedMatcher();
            case LOCATION -> new LocationBasedMatcher();
        };
    }
}

