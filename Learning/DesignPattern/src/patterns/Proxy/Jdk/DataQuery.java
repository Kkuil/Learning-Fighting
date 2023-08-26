package src.patterns.Proxy.Jdk;

/**
 * @Author Kkuil
 * @Date 2023/8/26 16:47
 * @Description 数据查询接口
 */
public interface DataQuery {
    /**
     * 查询数据
     *
     * @param id 数据id
     * @return 数据
     */
    String queryData(String id);
}
