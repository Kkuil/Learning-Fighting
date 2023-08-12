package src.Liskov;

public class Test02 {
    public static void main(String[] args) {
        Aa a = new Aa();
        System.out.println("11-3=" + a.func1(11, 3));
        System.out.println("1-8=" + a.func1(1, 8));
        System.out.println("-----------------------");
        Bb b = new Bb();
        System.out.println("11+3=" + b.func1(11, 3));
        System.out.println("1+8=" + b.func1(1, 8));
        System.out.println("11+3+9=" + b.func2(11, 3));
        System.out.println("11+3+100=" + b.func3(11, 3));
    }
}

class Base {

}

class Aa extends Base {
    public int func1(int num1, int num2) {
        return num1 - num2;
    }
}

class Bb extends Base {
    private Aa a = new Aa();

    public int func1(int a, int b) {
        return a + b;
    }

    public int func2(int a, int b) {
        return this.func1(a, b) + 9;
    }

    public int func3(int a, int b) {
        return this.a.func1(a, b) + 100;
    }
}
