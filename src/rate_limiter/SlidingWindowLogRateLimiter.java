package rate_limiter;

import java.util.LinkedList;
import java.util.Queue;

class SlidingWindowLogRateLimiter {

    private final int limit;
    private final long windowSizeMs;

    private final Queue<Long> timestamps = new LinkedList<>();

    public SlidingWindowLogRateLimiter(
            int limit,
            long windowSizeMs) {

        this.limit = limit;
        this.windowSizeMs = windowSizeMs;
    }

    public synchronized boolean allowRequest() {

        long now = System.currentTimeMillis();

        while (!timestamps.isEmpty()
                && now - timestamps.peek() >= windowSizeMs) {

            timestamps.poll();
        }

        if (timestamps.size() >= limit) {
            return false;
        }

        timestamps.offer(now);

        return true;
    }
}