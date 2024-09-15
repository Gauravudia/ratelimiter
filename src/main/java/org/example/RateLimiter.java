package org.example;

public interface RateLimiter {
    boolean tryAcquire();
}
