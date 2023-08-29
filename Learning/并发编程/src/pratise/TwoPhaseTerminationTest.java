package src.pratise;

public class TwoPhaseTerminationTest {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();

        Thread.sleep(3500);
        twoPhaseTermination.stop();
    }
}

class TwoPhaseTermination {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    System.out.println("线程被中断了");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    System.out.println(System.currentTimeMillis() + "执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    current.interrupt();
                }
            }
        }, "monitor");
        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}
