package com.kkuil.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

import java.lang.reflect.Proxy;

@Component
public class KkuilBeanPostProcess implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(KkuilBeanPostProcess.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                System.out.println("代理逻辑");
                return method.invoke(bean, args);
            });
            return proxyInstance;
        }
        return bean;
    }
}
