# Rate Limiters

This folder implements four ways to decide whether a request should be admitted. Each class exposes `allowRequest()`, returning `true` to allow the request and `false` to reject it.

Each instance owns one limit. There is no user/API-key lookup, HTTP middleware, or shared store. The examples are useful for comparing the state each algorithm needs and the traffic patterns it permits.

## Implementations

| Class | State and behavior | Main trade-off |
| --- | --- | --- |
| `FixedWindowRateLimiter` | Counts accepted requests; resets when an arriving request finds the window elapsed | Constant space, but traffic can burst around a window boundary |
| `SlidingWindowLogRateLimiter` | Queues accepted timestamps and removes entries outside the rolling window | More precise rolling-window accounting at the cost of timestamp storage |
| `TokenBucketRateLimiter` | Starts full, replenishes tokens up to capacity, consumes one per accepted request | Allows accumulated capacity to be spent in a burst |
| `LeakyBucketRateLimiter` | Tracks water level, subtracts elapsed leakage, and adds one per accepted request | Models draining occupancy, but does not actually queue or pace execution |

## Timing details

Fixed and sliding windows take `windowSizeMs` in milliseconds. Token refill and bucket leakage rates are units per second. All implementations use `System.currentTimeMillis()` and update their state lazily when a request arrives; none requires a background timer.

The fixed window starts at construction and resets its start to the next request's time after expiry. It is not aligned to calendar boundaries. The sliding log removes timestamps whose age is at least the window size, and rejected requests are not added to the log.

The token bucket begins with `capacity` tokens. For example, capacity 10 and refill rate 2 allow an initial burst of ten requests, then replenish at two tokens per second, capped at ten.

The leaky bucket begins empty. One implementation detail matters: it rejects when `water >= capacity`, then adds one. If the level is fractional and just below capacity, an accepted request can push it above capacity. A strict-capacity version would need to check the post-admission level.

## Complexity and concurrency

Every `allowRequest()` method is synchronized, making the state update atomic within that limiter instance. This does not coordinate limits across instances or processes.

Fixed window and both bucket implementations use O(1) time and space per request. Sliding-window cleanup can remove O(n) timestamps in one call, with amortized O(1) queue work per accepted request over its lifetime. Stored timestamps are bounded by the configured limit for a valid positive configuration.

## Try it

There is no `Main`. `TokenBucketRateLimiter` is public and can be used from another package:

```java
TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 2.0);
boolean accepted = limiter.allowRequest();
```

The other three classes are package-private, so a driver comparing all four should declare `package rate_limiter;`.

## Current boundaries

Constructors do not validate capacities, limits, windows, or rates. Clock changes can also distort elapsed-time calculations. Useful follow-ups are parameter validation, an injectable monotonic time source, and boundary-focused examples. Distributed enforcement would need a separate design for atomic updates and shared time assumptions.
