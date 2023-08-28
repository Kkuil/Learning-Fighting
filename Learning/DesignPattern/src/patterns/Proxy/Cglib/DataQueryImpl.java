package src.patterns.Proxy.Cglib;

/**
 * @Author Kkuil
 * @Date 2023/8/26 16:56
 * @Description 数据库查询实现
 */
public class DataQueryImpl implements DataQuery {
    /**
     * 查询数据
     *
     * @param id 数据id
     * @return 数据
     */
    @Override
    public String queryData(String id) {
        return "data from database";
    }
}
