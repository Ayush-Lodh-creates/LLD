package dating_app.strategy;

import dating_app.entities.Location;
import dating_app.entities.User;

import java.util.List;

public interface LocationStrategy {
    List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers);
}

