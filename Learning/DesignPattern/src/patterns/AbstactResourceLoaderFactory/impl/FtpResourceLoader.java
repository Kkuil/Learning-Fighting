package src.patterns.AbstactResourceLoaderFactory.impl;

import src.patterns.AbstactResourceFactory.AbstractResourceFactory;
import src.patterns.AbstactResourceLoaderFactory.AbstractResourceLoaderFactory;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:06
 * @Description Ftp资源加载器
 */
public class FtpResourceLoader extends AbstractResourceLoaderFactory {
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
