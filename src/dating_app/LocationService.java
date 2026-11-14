package dating_app;

import dating_app.entities.Location;
import dating_app.entities.User;
import dating_app.strategy.BasicLocationStrategy;
import dating_app.strategy.LocationStrategy;

import java.util.List;

public class LocationService {
    private static final LocationService INSTANCE = new LocationService();
    private LocationStrategy strategy = new BasicLocationStrategy();

    private LocationService() {}
    public static LocationService getInstance() { return INSTANCE; }
    public void setStrategy(LocationStrategy strategy) { this.strategy = strategy; }

    public List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers) {
        return strategy.findNearbyUsers(location, maxDistance, allUsers);
    }
}

