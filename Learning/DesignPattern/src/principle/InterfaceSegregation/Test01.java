package src.principle.InterfaceSegregation;

public class Test01 {
    public static void main(String[] args) {
        System.out.println("-------------");
        C c = new C();
        c.depend1(new A());
        c.depend2(new A());
        c.depend3(new A());
        System.out.println("-------------");
        D d = new D();
        d.depend1(new B());
        d.depend4(new B());
        d.depend5(new B());
    }
}

interface interface1 {
    void operation1();

    void operation2();

    void operation3();

    void operation4();

    void operation5();
}

class A implements interface1 {

    @Override
    public void operation1() {
        System.out.println("A 实现了 operation1");
    }

    @Override
    public void operation2() {
        System.out.println("A 实现了 operation2");
    }

    @Override
    public void operation3() {
        System.out.println("A 实现了 operation3");
    }

    @Override
    public void operation4() {
        System.out.println("A 实现了 operation4");
    }

    @Override
    public void operation5() {
        System.out.println("A 实现了 operation5");
    }
}

class B implements interface1 {
    @Override
    public void operation1() {
        System.out.println("B 实现了 operation1");
    }

    @Override
    public void operation2() {
        System.out.println("B 实现了 operation2");
    }

    @Override
    public void operation3() {
        System.out.println("B 实现了 operation3");
    }

    @Override
    public void operation4() {
        System.out.println("B 实现了 operation4");
    }

    @Override
    public void operation5() {
        System.out.println("B 实现了 operation5");
    }
}

class C {
    public void depend1(interface1 i) {
        i.operation1();
    }

    public void depend2(interface1 i) {
        i.operation2();
    }

    public void depend3(interface1 i) {
        i.operation3();
    }
}

class D {
    public void depend1(interface1 i) {
        i.operation1();
    }

    public void depend4(interface1 i) {
        i.operation4();
    }

    public void depend5(interface1 i) {
        i.operation5();
    }
}
