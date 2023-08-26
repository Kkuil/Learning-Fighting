package src.patterns.Factory.AbstactResourceLoaderFactory;

import src.patterns.Factory.AbstactResourceFactory.AbstractResourceFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:00
 * @Description 抽象资源加载工厂
 */
public abstract class AbstractResourceLoaderFactory {

    /**
     * 加载资源
     * @param resource 资源
     * @return 加载好的资源
     */
    public abstract AbstractResourceFactory load(AbstractResourceFactory resource);
}
