package src;

import java.util.ArrayList;

public class LocalThreadSafeQuestions {
    public static final int LOOP_NUMBER = 200;
    public static final int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        ThreadSubSafe threadSubSafe = new ThreadSubSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadSubSafe.method1(LOOP_NUMBER);
            }, "Thread" + i).start();
        }
    }
}

class ThreadUnsafe {
    ArrayList<String> list = new ArrayList();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    public void method2() {
        list.add("1");
    }

    public void method3() {
        list.remove(0);
    }
}

class ThreadSafe {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list) {
        list.add("1");
    }

    public void method3(ArrayList<String> list) {
        list.remove(0);
    }
}

class ThreadSubSafe extends ThreadSafe {
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}
