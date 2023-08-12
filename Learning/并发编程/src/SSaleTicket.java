package src;

class Ticket {
    private int number = 30;

    public synchronized void sale() throws InterruptedException {
        if (number > 0) {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票，剩余：" + number);
        }
    }
}

public class SSaleTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    ticket.sale();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "CC").start();
    }
}
