package src.patterns.Factory.AbstactResourceLoaderFactory.impl;

import src.patterns.Factory.AbstactResourceFactory.AbstractResourceFactory;
import src.patterns.Factory.AbstactResourceLoaderFactory.AbstractResourceLoaderFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:06
 * @Description Classpath资源加载器
 */
public class ClasspathResourceLoader extends AbstractResourceLoaderFactory {
    /**
     * 加载资源
     *
     * @param resource 资源
     * @return 加载好的资源
     */
    @Override
    public AbstractResourceFactory load(AbstractResourceFactory resource) {
        return null;
    }
}
