package src;

import java.util.concurrent.locks.LockSupport;

public class LockParkTest {
    public static void main(String[] args) throws InterruptedException {
        park();
    }

    public static void park() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(System.currentTimeMillis() + "running...");
            System.out.println(System.currentTimeMillis() + "park");
            LockSupport.park();

            System.out.println(System.currentTimeMillis() + "unpark");
            System.out.println(System.currentTimeMillis() + "running...");

            Thread.interrupted();
            LockSupport.park();
            System.out.println(System.currentTimeMillis() + "unpark");
        }, "t1");
        thread.start();

        Thread.sleep(1000);
        thread.interrupt();
    }
}
