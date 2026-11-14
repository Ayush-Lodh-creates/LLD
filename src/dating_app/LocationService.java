package dating_app;

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
