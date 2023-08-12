package lock;

import java.util.concurrent.locks.ReentrantLock;

class Ticket {
    private int number = 30;

    private final ReentrantLock lock = new ReentrantLock();

    public void sale() throws InterruptedException {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票，剩余：" + number);
            } else {
                System.out.println(Thread.currentThread().getName() + "票卖完了");
                System.exit(0);
            }
        } finally {
            lock.unlock();
        }
    }
}

public class LSaleTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "CC").start();
    }
}
