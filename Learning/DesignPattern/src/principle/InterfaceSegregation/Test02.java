package src.principle.InterfaceSegregation;

public class Test02 {
    public static void main(String[] args) {
        System.out.println("-------------");
        Cc c = new Cc();
        c.depend1(new Aa());
        c.depend2(new Bb());
        c.depend3(new Bb());
        System.out.println("-------------");
        Dd d = new Dd();
        d.depend1(new Aa());
        d.depend2(new Aa());
        d.depend3(new Aa());
    }
}

interface Interface2 {
    void operation1();
}

interface Interface3 {
    void operation4();

    void operation5();
}

interface Interface4 {
    void operation2();

    void operation3();
}

class Aa implements Interface2, Interface3 {
    @Override
    public void operation1() {
        System.out.println("A 实现了 operation1");
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

class Bb implements Interface2, Interface4 {
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
}

class Cc {
    public void depend1(Interface2 i) {
        i.operation1();
    }

    public void depend2(Interface4 i) {
        i.operation2();
    }

    public void depend3(Interface4 i) {
        i.operation3();
    }
}

class Dd {
    public void depend1(Interface2 i) {
        i.operation1();
    }

    public void depend2(Interface3 i) {
        i.operation4();
    }

    public void depend3(Interface3 i) {
        i.operation5();
    }
}
