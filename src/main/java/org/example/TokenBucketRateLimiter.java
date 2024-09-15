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
    public synchronized boolean tryAcquire(){
       refill();
       if(currentBucketSize >0){
           currentBucketSize--;
           System.out.println("Request accepted!");
           return true;
       }
       System.out.println("Too many requests. Please try again.");
       return false;
    }

    public void refill(){
        long currentTimeStamp = System.currentTimeMillis();
        long noOfTokenToAdd = (long) (Math.floor((currentTimeStamp - lastRefillTimestamp)* refillRatePerMillis));
        if(noOfTokenToAdd > 0){
            System.out.println("Refilled no of tokens: "+ noOfTokenToAdd);
            lastRefillTimestamp = currentTimeStamp;
            currentBucketSize = Math.min(noOfTokenToAdd+currentBucketSize, maxBucketSize);
        }
    }
}
