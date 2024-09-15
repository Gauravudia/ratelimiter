package org.example;

public class TokenBucketRateLimiter implements RateLimiter {
    private final long maxBucketSize;
    private final double refillRatePerMillis;
    private long currentBucketSize;
    private long lastRefillTimestamp;

    TokenBucketRateLimiter(long bucketSize, long timeWindowMillis){
        maxBucketSize = bucketSize;
        refillRatePerMillis = (double) bucketSize / timeWindowMillis;
        currentBucketSize = maxBucketSize;
        lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public  boolean tryAcquire() {
        refill();
        synchronized (this) {
            if (currentBucketSize > 0) {
                currentBucketSize--;
                System.out.println("Request accepted!");
                return true;
            }
        }
        System.out.println("Too many requests. Please try again.");
        return false;
    }

    public void refill(){
        long currentTimeStamp = System.currentTimeMillis();
        // with timeWindowMillis division => long noOfTokenToAdd = (59000 / 60000) * 3 = 0 * 3 = 0 tokens
        // with refillRatePerMillis => long noOfTokenToAdd = 59000 * 0.00005 = 1 tokens, where refillRatePerMillis = 3/60000 = 0.00005
        long noOfTokenToAdd = (long) (Math.floor((currentTimeStamp - lastRefillTimestamp)* refillRatePerMillis));
        if(noOfTokenToAdd > 0){
            synchronized (this) {
                System.out.println("Refilled no of tokens: " + noOfTokenToAdd);
                lastRefillTimestamp = currentTimeStamp;
                currentBucketSize = Math.min(noOfTokenToAdd + currentBucketSize, maxBucketSize);
            }
        }
    }
}
