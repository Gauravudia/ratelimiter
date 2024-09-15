package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // refill after every minute, timeWindowMillis: 60000
        RateLimiter rateLimiter = new TokenBucketRateLimiter(3, 60000);
        Thread[] threads = new Thread[10];
        for(int i=0;i<10;i++){
            threads[i] = new Thread(()->{
                try {
                    rateLimiter.tryAcquire();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " was interrupted.");
                }
                System.out.println(Thread.currentThread().getName() + " has finished!");
            });
            threads[i].start();
        }

        for(int i=0;i<10;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted.");
            }
        }

        System.out.println("All threads have finished. Main thread is terminating.");
    }
}