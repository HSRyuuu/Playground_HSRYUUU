package com.hsryuuu.base.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 공정성(fairness) 차이 관찰.
 *
 *  - fair=true  → 줄 선 순서대로(1,2,3,1,2,3,...) 회전한다.
 *  - fair=false → 한 스레드가 연속해서 가로채는 "barging"(1,1,1,1,...,2,2,...)이 잘 보인다.
 *
 * 적은 수의 스레드가 락을 짧게 여러 번 반복 획득하도록 해 barging 가능성을 만든다.
 * (락을 길게 잡거나 임계 영역에서 sleep을 하면 다른 스레드가 모두 park 되어버려 fair/unfair 차이가 잘 안 보인다.)
 */
public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== fair=true (공정 락) ===");
        run(new TicketQueue(true));

        System.out.println();
        System.out.println("=== fair=false (비공정 락 / 기본값) ===");
        run(new TicketQueue(false));
    }

    private static void run(TicketQueue queue) throws InterruptedException {
        long[] users = {1L, 2L, 3L};
        int iterationsPerUser = 10;

        Thread[] threads = new Thread[users.length];
        for (int i = 0; i < users.length; i++) {
            long userId = users[i];
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterationsPerUser; j++) {
                    queue.book(userId);
                }
            });
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }
}

class TicketQueue {
    private final ReentrantLock lock;

    public TicketQueue(boolean fair) {
        this.lock = new ReentrantLock(fair);
    }

    public void book(long userId) {
        lock.lock();
        try {
            System.out.println("[" + userId + "] 진입");
        } finally {
            lock.unlock();
        }
    }
}
