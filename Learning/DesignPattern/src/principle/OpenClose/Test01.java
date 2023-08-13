package src.principle.OpenClose;

public class Test01 {
    public static void main(String[] args) {
        DrawGraphic dg = new DrawGraphic();
        dg.draw(new Circle());
        dg.draw(new Rectangle());
        dg.draw(new Triangle());
    }
}

class DrawGraphic {
    public void draw(Shape s) {
        s.draw();
    }
}

class Shape {
    public void draw() {

    }
}

class Circle extends Shape {
    public void draw() {
        System.out.println("绘制圆形");
    }
}

class Rectangle extends Shape {
    public void draw() {
        System.out.println("绘制矩形");
    }
}

class Triangle extends Shape {
    public void draw() {
        System.out.println("绘制三角形");
    }
}
