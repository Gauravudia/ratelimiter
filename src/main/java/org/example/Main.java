package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // refill after every minute, timeWindowMillis: 60000
        RateLimiter rateLimiter = new TokenBucketRateLimiter(3, 60000);
        for (int i = 0; i < 10; i++) {
            rateLimiter.tryAcquire();
            Thread.sleep(10000);
        }
    }
}