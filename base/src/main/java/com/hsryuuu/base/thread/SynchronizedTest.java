package com.hsryuuu.base.thread;

public class SynchronizedTest {
    public static void main(String[] args) throws InterruptedException {
        TicketBooth ticketBooth = new TicketBooth();

        long[] users = {1L, 2L, 3L, 4L, 5L};
        Thread[] threads = new Thread[users.length];

        for(int i = 0; i < users.length; i++){
            long userId = users[i];
            threads[i] = new Thread(() -> ticketBooth.book(userId));
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("최종 남은 티켓: " + ticketBooth.remainingTickets);
    }

}

class TicketBooth{
    int remainingTickets = 3;

    public synchronized void book(long userId){
        if (remainingTickets > 0) {
            try {
                System.out.println("[" + userId + "] 예매 가능 확인! 결제 진행 중...");
                Thread.sleep(1000);  // 결제 처리 (PG사 통신 등)
                remainingTickets--;
                System.out.println("[" + userId + "] 예매 완료! 남은 티켓: " + remainingTickets);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("[" + userId + "] 매진");
        }
    }
}
