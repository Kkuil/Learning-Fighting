package src.SingleResposibility;

public class Test01 {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle();
        vehicle.run("小汽车");
        vehicle.run("飞机");
        vehicle.run("轮船");
    }
}

class Vehicle {
    public void run(String vehicle) {
        System.out.println(vehicle + "在公路上运行...");
    }
}
