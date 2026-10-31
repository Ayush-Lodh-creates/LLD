package rate_limiter;

public class TokenBucketRateLimiter {

    private final int capacity;
    private final double refillRate;

    private double tokens;
    private long lastRefillTime;

    public TokenBucketRateLimiter(
            int capacity,
            double refillRate) {

        this.capacity = capacity;
        this.refillRate = refillRate;

        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        long now = System.currentTimeMillis();

        double elapsedSeconds =
                (now - lastRefillTime) / 1000.0;

        tokens = Math.min(
                capacity,
                tokens + elapsedSeconds * refillRate
        );

        lastRefillTime = now;

        if (tokens < 1) {
            return false;
        }

        tokens--;

        return true;
    }
}
