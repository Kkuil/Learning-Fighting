package src.patterns.Factory.AbstactResourceFactory.impl;

import src.patterns.Factory.AbstactResourceFactory.AbstractResourceFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:11
 * @Description Classpath资源
 */
public class ClasspathResource extends AbstractResourceFactory {
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
