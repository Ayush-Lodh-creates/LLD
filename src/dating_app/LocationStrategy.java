package dating_app;

import java.util.List;

public interface LocationStrategy {
    List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers);
}
