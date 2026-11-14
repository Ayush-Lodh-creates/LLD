package dating_app;

import java.util.List;

public class Preference {
    private final int minAge;
    private final int maxAge;
    private final double maxDistance;
    private final List<String> interests;
    private final List<Gender> interestedIn;

    public Preference(int minAge, int maxAge, double maxDistance,
                      List<String> interests, List<Gender> interestedIn) {
        if (minAge < 18 || maxAge < minAge || maxDistance < 0) {
            throw new IllegalArgumentException("Invalid age range or distance");
        }
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxDistance = maxDistance;
        this.interests = List.copyOf(interests);
        this.interestedIn = List.copyOf(interestedIn);
    }

    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
    public double getMaxDistance() { return maxDistance; }
    public List<String> getInterests() { return interests; }
    public List<Gender> getInterestedIn() { return interestedIn; }

    // Empty gender list means any gender. Interests are used for ranking.
    public boolean meets(UserProfile profile) {
        return profile.getAge() >= minAge && profile.getAge() <= maxAge
                && (interestedIn.isEmpty() || interestedIn.contains(profile.getGender()));
    }
}
