# LRU Cache with TTL

This cache combines a capacity limit with entry expiry. When space is needed, it removes the least recently used live entry. Each entry also has a fixed lifetime measured from its most recent write.

## Contract

`Cache(int size, long validity)` accepts a positive capacity and a positive TTL in milliseconds. Keys and values are integers. `put(key, value)` inserts or updates an entry; `get(key)` returns its value or `null` on a miss.

Reading an entry makes it most recently used but does not extend its TTL. Updating an existing key refreshes both its position and expiry.

## Data structures

[`Cache`](Cache.java) uses a `HashMap<Integer, Node>` for lookup and a doubly linked list for recency. [`Node`](Node.java) stores the key, value, expiry timestamp, and neighboring links.

```text
head <-> most recently used <-> ... <-> least recently used <-> tail
```

The head and tail are sentinel nodes. They avoid separate insertion and removal logic for the first or last real entry. The map points directly to list nodes, so finding an entry does not require walking the list.

## Operation flow

Both public operations first call `evict()` to remove expired entries.

For `get()`, a missing key returns `null`. A hit is detached from its current position and inserted just after the head.

For `put()`, an existing node gets its value and expiry updated and moves to the front. A new key evicts the node before the tail if the cache is still full, then inserts a new node at the front.

## Complexity and concurrency

Map lookup and list relinking are O(1) on average. However, this implementation scans the entire map for expiry on every read and write, making complete operations O(n), where n is the number of cached entries. Space is O(n), bounded by capacity during normal operation.

`get()` and `put()` are synchronized on the cache instance. This protects the map and linked list as one unit, but serializes all callers. Expiry is lazy: idle entries stay in memory until another operation triggers cleanup.

## Known issue

`evict()` removes entries with `keyNodeMap.remove()` while iterating over the same `HashMap` using an enhanced for-loop. When iteration continues after a removal, this can throw `ConcurrentModificationException`. Synchronizing the public methods does not solve mutation through an invalidated iterator. Iterator-based removal is a necessary follow-up before relying on expiry behavior.

## Try it

There is no package-level `Main`. This sequence can be placed in a driver with `import lru_cache.Cache;`:

```java
Cache cache = new Cache(2, 60_000);
cache.put(1, 10);
cache.put(2, 20);
System.out.println(cache.get(1)); // 10; key 1 becomes most recently used
cache.put(3, 30);                // evicts key 2
System.out.println(cache.get(2)); // null
```

The example exercises recency without waiting for expiry. TTL cleanup should be checked separately after fixing the iterator issue. A useful design discussion is whether expiry should use a periodic scan, a separate expiry queue, or only cleanup of accessed entries.
