package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // refill after every minute, timeWindowMillis: 60000
        RateLimiter rateLimiter = new TokenBucketRateLimiter(3, 60000);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Submit 3 tasks that should consume all tokens
        submitTasks(executorService, rateLimiter, 3, "initial");

        // Simulate time passing for 59 seconds before submitting more tasks
        System.out.println("Waiting 59 seconds for tokens to refill...");
        Thread.sleep(59000); // Wait 59 seconds to simulate token refill timing

        // Submit 3 more tasks after tokens should have been refilled
        System.out.println("Submitting new tasks after waiting for token refill.");
        submitTasks(executorService, rateLimiter, 3, "after refill");

        executorService.shutdown();
    }

    private static void submitTasks(ExecutorService executorService, RateLimiter rateLimiter, int numberOfTasks, String batchName) {
        for (int i = 0; i < numberOfTasks; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                try {
                    boolean success = rateLimiter.tryAcquire();
                    if (success) {
                        Thread.sleep(1000); // Simulate some work for 1 second
                        System.out.println("Task " + taskId + " from " + batchName + " batch finished a task.");
                    } else {
                        System.out.println("Task " + taskId + " from " + batchName + " batch rejected due to rate limiting.");
                    }
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskId + " from " + batchName + " batch was interrupted.");
                }
            });
        }
    }
}