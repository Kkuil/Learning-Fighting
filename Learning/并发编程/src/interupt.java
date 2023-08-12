package src;

public class interupt {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被中断了");
                    break;
                }
            }
        }, "t1");

        t1.start();
        System.out.println("interrupt");
        t1.interrupt();
    }
}
