package src.pratise;

public class yieldAndPriority {
    public static void main(String[] args) {
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                int count = 1;
                while (true) {
                    count++;
                    System.out.println(Thread.currentThread().getName() + "----------------------------------------:" + count);
                }
            }
        };

        Runnable task2 = new Runnable() {
            @Override
            public void run() {
//                Thread.yield();
                int count = 1;
                while (true) {
                    count++;
                    System.out.println(Thread.currentThread().getName() + ":" + count);
                }
            }
        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }
}
