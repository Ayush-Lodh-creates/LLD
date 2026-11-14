package dating_app.entities;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Haversine formula: distance between two coordinates in kilometres.
    public double distanceInKm(Location other) {
        double latDifference = Math.toRadians(other.latitude - latitude);
        double lonDifference = Math.toRadians(other.longitude - longitude);
        double a = Math.pow(Math.sin(latDifference / 2), 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.pow(Math.sin(lonDifference / 2), 2);
        a = Math.max(0, Math.min(1, a));
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}

