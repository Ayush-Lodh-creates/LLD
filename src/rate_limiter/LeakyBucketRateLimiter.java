package rate_limiter;

class LeakyBucketRateLimiter {

    private final int capacity;
    private final double leakRate;

    private double water;
    private long lastLeakTime;

    public LeakyBucketRateLimiter(
            int capacity,
            double leakRate) {

        this.capacity = capacity;
        this.leakRate = leakRate;

        this.water = 0;
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        long now = System.currentTimeMillis();

        double elapsedSeconds =
                (now - lastLeakTime) / 1000.0;

        water = Math.max(
                0,
                water - elapsedSeconds * leakRate
        );

        lastLeakTime = now;

        if (water >= capacity) {
            return false;
        }

        water++;

        return true;
    }
}