# Rate Limiter
ref: 
- https://medium.com/geekculture/system-design-design-a-rate-limiter-81d200c9d392
- https://medium.com/@devenchan/implementing-rate-limiting-in-java-from-scratch-leaky-bucket-and-tokenn-bucket-implementation-63a944ba93aa

## Token Bucket Rate Limiter
This project implements a **Token Bucket Rate Limiter** in Java. The Token Bucket algorithm is used to control the rate at which requests are allowed. It works by allowing a fixed number of tokens in a bucket, which are replenished at a constant rate. Each request consumes a token, and when no tokens are available, the request is rejected.

## Implementation Overview

The implementation evolved over several iterations to support a multithreaded environment, optimize synchronization, and improve thread management.

---

## Iteration 1: Initial Implementation

In the initial version, the rate limiter was implemented as a simple **Token Bucket**, without considering multithreading or synchronization issues.

- **Key Details**:
  - Tokens were stored in a bucket.
  - The bucket was refilled at a constant rate based on the time passed.
  - Requests could acquire tokens as long as tokens were available.

This version did **not** account for synchronization, so it was unreliable in a multithreaded environment.

---

## Iteration 2: Adding Multithreading with Synchronization

In this iteration, we introduced a **multithreaded environment**. To ensure thread safety, we added **synchronization** to the rate-limiting logic.

- **Key Changes**:
  - **Synchronized Methods**: The entire `tryAcquire()` and `refill()` methods were synchronized to ensure only one thread could modify the token count and timestamp at any given time.
  - **Thread-Safety**: This prevented multiple threads from concurrently accessing and modifying shared variables.

While this ensured correctness, synchronizing entire methods reduced performance, especially in high-concurrency scenarios.

---

## Iteration 3: Optimizing Locking for Critical Sections

In this iteration, we optimized the locking mechanism by reducing the scope of the synchronized blocks to only the critical sections that modified shared resources (`currentBucketSize` and `lastRefillTimestamp`).

- **Key Changes**:
  - **Partial Synchronization**: Instead of locking the entire method, only the sections that modify `currentBucketSize` and `lastRefillTimestamp` were synchronized.
  - **Improved Performance**: This approach allowed more concurrency while maintaining thread safety, reducing contention between threads.

---

## Iteration 4: Using ExecutorService for Thread Management

In this final iteration, we switched to using the **`ExecutorService`** for managing threads more efficiently, instead of creating and managing threads manually.

- **Key Changes**:
  - **Thread Pool Management**: By utilizing `ExecutorService`, we can better manage thread lifecycles, reuse threads, and control the number of concurrent threads.
  - **Scalability**: The use of `ExecutorService` allows better scaling by optimizing resource usage and simplifying thread management.

---
