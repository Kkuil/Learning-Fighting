package src.patterns.Proxy.Cglib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/8/26 16:49
 * @Description 数据查询处理器
 */
public class DataQueryInvocationHandler implements InvocationHandler {
    private final Map<String, Object> cacheMap = new HashMap<>();

    private final DataQuery dataQuery = new DataQueryImpl();

    @Override
    public Object invoke(Object o, Method method, Object[] params) throws Throwable {
        // 1. 判断缓存中是否存在数据
        if (cacheMap.containsKey(params[0])) {
            System.out.println("从缓存中获取数据");
            return cacheMap.get(params[0]);
        }

        // 2. 从数据库中查询数据
        System.out.println("从数据库中获取数据");
        Object result = method.invoke(dataQuery, params);
        // 2.1 将数据存入缓存
        cacheMap.put(params[0].toString(), result);
        return null;
    }
}
