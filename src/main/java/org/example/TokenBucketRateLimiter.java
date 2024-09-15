package org.example;

public class TokenBucketRateLimiter implements RateLimiter {
    private final long maxBucketSize;
    private final long timeWindowMillis;
    private long currentBucketSize;
    private long lastRefillTimestamp = System.currentTimeMillis();

    TokenBucketRateLimiter(long bucketSize, long timeWindowMillis){
        this.maxBucketSize = bucketSize;
        this.timeWindowMillis = timeWindowMillis;
        currentBucketSize = maxBucketSize;
    }

    @Override
    public boolean tryAcquire(){
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
        long noOfTokenToAdd = ((currentTimeStamp - lastRefillTimestamp)/ timeWindowMillis)* maxBucketSize;
        if(noOfTokenToAdd > 0){
            System.out.println("Refilled no of tokens: "+ noOfTokenToAdd);
            lastRefillTimestamp = currentTimeStamp;
            currentBucketSize = Math.min(noOfTokenToAdd+currentBucketSize, maxBucketSize);
        }
    }
}
