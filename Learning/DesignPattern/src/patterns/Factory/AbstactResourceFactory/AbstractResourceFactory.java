package src.patterns.Factory.AbstactResourceFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:04
 * @Description 抽象资源工厂
 */
public abstract class AbstractResourceFactory {

    /**
     * 加载资源前缀
     *
     * @return 前缀
     */
    public abstract String getPrefix();
}
