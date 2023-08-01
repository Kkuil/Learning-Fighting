package com.kkuil.service;

import com.kkuil.annotation.KkuilValue;
import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

@Component
public class KkuilValueBeanPostProcess implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            KkuilValue annotation = field.getAnnotation(KkuilValue.class);
            if (annotation != null) {
                String value = annotation.value();
                field.setAccessible(true);
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
