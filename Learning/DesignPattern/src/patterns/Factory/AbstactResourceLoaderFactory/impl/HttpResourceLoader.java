package src.patterns.Factory.AbstactResourceLoaderFactory.impl;

import src.patterns.Factory.AbstactResourceFactory.AbstractResourceFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:06
 * @Description Http资源加载器
 */
public class HttpResourceLoader extends AbstractResourceFactory {
    /**
     * 加载资源前缀
     *
     * @return 前缀
     */
    @Override
    public String getPrefix() {
        return null;
    }
}
