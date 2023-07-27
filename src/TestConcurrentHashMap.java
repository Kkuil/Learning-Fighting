package src;

public class TestConcurrentHashMap {
    public static void main(String[] args) {
        java.util.concurrent.ConcurrentHashMap<Object, Object> map = new java.util.concurrent.ConcurrentHashMap<>();
        map.put("1", "1");
        Object compute = map.compute("1", (k, v) -> {
            System.out.println(k);
            System.out.println(v);
            return "2";
        });
        System.out.println(compute);
    }
}
