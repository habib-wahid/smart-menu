package org.example.menuapp.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RateLimiterConfig {
    private final Map<String, Bucket> buckets = new HashMap<>();

    public RateLimiterConfig() {
        buckets.put("/api/category", createBucket());
    }

    private Bucket createBucket() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(2)
                .refillGreedy(5, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }

    public Bucket getBucket(String key) {
        return buckets.get(key);
    }
}
