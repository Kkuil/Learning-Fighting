package src.Test.patterns.AbstractFactory;

import src.patterns.Factory.AbstactResourceFactory.AbstractResourceFactory;
import src.patterns.Factory.AbstactResourceFactory.impl.FtpResource;
import src.patterns.Factory.AbstactResourceLoaderFactory.impl.FtpResourceLoader;

/**
 * @Author Kkuil
 * @Date 2023/8/13 19:14
 * @Description 测试抽象工厂的测试类
 */
public class TestAbstractFactory {
    public static void main(String[] args) {
        FtpResourceLoader ftpResourceLoader = new FtpResourceLoader();
        FtpResource ftpResource = new FtpResource();
        AbstractResourceFactory factory = ftpResourceLoader.load(ftpResource);
    }
}
