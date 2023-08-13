package src.principle.SingleResposibility;

public class Test03 {
    public static void main(String[] args) {
        Vehicle2 vehicle2 = new Vehicle2();
        vehicle2.runOnRoad("小汽车");
        vehicle2.runOnAir("飞机");
        vehicle2.runOnWater("轮船");
    }
}

class Vehicle2 {
    public void runOnRoad(String vehicle) {
        System.out.println(vehicle + "在公路上运行...");
    }

    public void runOnAir(String vehicle) {
        System.out.println(vehicle + "在天上上运行...");
    }

    public void runOnWater(String vehicle) {
        System.out.println(vehicle + "在水中上运行...");
    }
}
