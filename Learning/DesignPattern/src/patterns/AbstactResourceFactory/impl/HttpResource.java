package src.patterns.AbstactResourceFactory.impl;

import src.patterns.AbstactResourceFactory.AbstractResourceFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:08
 * @Description Http资源
 */
public class HttpResource extends AbstractResourceFactory {
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
