package com.hsryuuu.base.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    public static void main(String[] args) throws InterruptedException {
        LockTicketBooth ticketBooth = new LockTicketBooth(true);

        int userCount = 100;
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < userCount; i++){
            long userId = i + 1;
            threads.add(new Thread(() -> ticketBooth.book(userId)));
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("최종 남은 티켓: " + ticketBooth.remainingTickets);
    }

}

class LockTicketBooth {

    private final ReentrantLock lock;

    public LockTicketBooth(boolean fair) {
        this.lock = new ReentrantLock(fair);
    }

    int remainingTickets = 50;

    public void book(long userId){
        lock.lock();
        if(remainingTickets <= 0){
            System.out.println("[" + userId + "] 매진");
            lock.unlock();
            return;
        }

        try {
            Thread.sleep(200);
            remainingTickets--;
            System.out.println("[" + userId + "] 예매 완료! 남은 티켓: " + remainingTickets);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }finally {
            lock.unlock();
        }
    }
}
