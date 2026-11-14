package dating_app;

import java.util.ArrayList;
import java.util.List;

public class BasicLocationStrategy implements LocationStrategy {
    @Override
    public List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers) {
        List<User> nearby = new ArrayList<>();
        for (User user : allUsers) {
            if (location.distanceInKm(user.getProfile().getLocation()) <= maxDistance) nearby.add(user);
        }
        return nearby;
    }
}
