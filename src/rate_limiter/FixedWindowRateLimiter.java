package rate_limiter;

class FixedWindowRateLimiter {

    private final int limit;
    private final long windowSizeMs;

    private long currentWindowStart;
    private int counter;

    public FixedWindowRateLimiter(int limit, long windowSizeMs) {
        this.limit = limit;
        this.windowSizeMs = windowSizeMs;
        this.currentWindowStart = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        long now = System.currentTimeMillis();

        if (now - currentWindowStart >= windowSizeMs) {
            currentWindowStart = now;
            counter = 0;
        }

        if (counter >= limit) {
            return false;
        }

        counter++;
        return true;
    }
}