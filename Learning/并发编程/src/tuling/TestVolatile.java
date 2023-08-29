package src.tuling;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author Kkuil
 * @Date 2023/8/29 19:58
 * @Description 测试线程间的共享变量的可见性问题
 */
public class TestVolatile {

    // private volatile static boolean flag = true;
    private static boolean flag = true;
    private static int count = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            while (flag) {
                count++;
                // System.out.println("count = " + count);
                // Thread.yield();
                // LockSupport.unpark(Thread.currentThread());
                // Thread.sleep(100);
            }
        }).start();

        try {
            Thread.sleep(1000);
            new Thread(() -> {
                flag = false;
                System.out.println("刷新flag = " + flag);
            }).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void plusCount() {

    }
}
