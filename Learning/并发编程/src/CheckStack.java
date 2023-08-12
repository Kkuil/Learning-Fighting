package src;

public class CheckStack {
    public static void main(String[] args) {
        new Thread(() -> {
            method1();
        }, "t1").start();
        method1();
    }

    public static void method1() {
        System.out.println(123);
        Object o = method2();
        System.out.println(o);
    }

    public static Object method2() {
        Object o = new Object();
        return o;
    }
}
