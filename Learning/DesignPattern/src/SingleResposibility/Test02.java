package src.SingleResposibility;

public class Test02 {
    public static void main(String[] args) {
        new RoadVehicle().run("小汽车");
        new AirVehicle().run("小汽车");
        new WaterVehicle().run("小汽车");
    }
}

class RoadVehicle {
    public void run(String vehicle) {
        System.out.println(vehicle + "在公路上运行...");
    }
}

class AirVehicle {
    public void run(String vehicle) {
        System.out.println(vehicle + "在天空上运行...");
    }
}

class WaterVehicle {
    public void run(String vehicle) {
        System.out.println(vehicle + "在水中运行...");
    }
}