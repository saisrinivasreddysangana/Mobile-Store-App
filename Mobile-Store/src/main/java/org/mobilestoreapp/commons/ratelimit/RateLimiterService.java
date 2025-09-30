// RateLimiterService.java
package org.mobilestoreapp.commons.ratelimit;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static class Counter {
        int count;
        Instant windowStart;
        Counter(int c, Instant w) { count = c; windowStart = w; }
    }

    private final Map<String, Counter> counters = new ConcurrentHashMap<>();

    public synchronized boolean allow(String key, int limit, long windowSeconds) {
        Instant now = Instant.now();
        Counter c = counters.get(key);
        if (c == null || now.isAfter(c.windowStart.plusSeconds(windowSeconds))) {
            counters.put(key, new Counter(1, now));
            return true;
        }
        if (c.count < limit) {
            c.count++;
            return true;
        }
        return false;
    }
}
