package src;

public class synchronize {
    public static void main(String[] args) {
        Room room = new Room();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                room.increment();
            }
        }, "t1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                room.decrement();
            }
        }, "t2");

        thread1.start();
        thread2.start();
        System.out.println(room.getCounter());
    }
}

class Room {
    private int counter = 0;

    public void increment() {
        synchronized (this) {
            this.counter++;
        }
    }

    public void decrement() {
        synchronized (this) {
            this.counter--;
        }
    }

    public int getCounter() {
        synchronized (this) {
            return this.counter;
        }
    }
}
