package src.patterns.Proxy.Jdk;

import java.lang.reflect.Proxy;

/**
 * @Author Kkuil
 * @Date 2023/8/26 16:47
 * @Description main方法
 */
public class Main {
    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        DataQuery dataQuery = (DataQuery) Proxy.newProxyInstance(loader, new Class[]{DataQuery.class}, new DataQueryInvocationHandler());
        System.out.println("dataQuery.queryData(\"1\") = " + dataQuery.queryData("1"));
        System.out.println("dataQuery.queryData(\"1\") = " + dataQuery.queryData("1"));
        System.out.println("dataQuery.queryData(\"2\") = " + dataQuery.queryData("2"));
    }
}
