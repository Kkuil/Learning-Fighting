package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KkuilApplicationContext {

    // 单例
    public static final String SCOPE_SINGLETON = "singleton";

    // 配置类
    private Class configClass;
    // beanDefinitionMap
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();
    // beanPostProcessor list
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public KkuilApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;

        // 1. 扫包
        scan(configClass);

        // 2. 遍历beanDefinitionMap，创建单例bean
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (SCOPE_SINGLETON.equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        });
    }

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @return bean
     */
    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new RuntimeException("不存在该bean");
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        boolean isSingleton = SCOPE_SINGLETON.equals(beanDefinition.getScope());
        if (isSingleton) {
            // 单例
            Object singletonBean = singletonObjects.get(beanName);
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            // 原型
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }

    /**
     * 扫包
     *
     * @param configClass 配置类
     * @throws ClassNotFoundException 找不到类异常
     */
    private void scan(Class configClass) throws ClassNotFoundException {
        // 1. 判断当前配置类上是否有@ComponentScan注解，并获取该注解的value值
        String path = getComponentScanPath(configClass);
        // 2. 扫描该路径下的所有带有Component注解的类
        // 2.1 获取当前类加载器
        ClassLoader loader = KkuilApplicationContext.class.getClassLoader();
        // 2.2 获取当前路径下的所有文件
        URL resource = loader.getResource(path);
        File fileObj = new File(resource.getFile());
        // 2.3 判断是否是文件夹
        if (!fileObj.isDirectory()) {
            throw new RuntimeException("扫描路径不是一个文件夹");
        }
        // 2.4 遍历文件夹下的所有文件
        for (File file : fileObj.listFiles()) {
            String classPath = file.getAbsolutePath();
            classPath = classPath
                    .substring(classPath.indexOf("com"), classPath.indexOf(".class"))
                    .replace("\\", ".");
            Class<?> clazz = loader.loadClass(classPath);
            // 2.5 判断是否有Component注解
            boolean isExistsAnnotationComponent = clazz.isAnnotationPresent(Component.class);
            // 2.6 判断是否有Scope注解
            boolean isExistsAnnotationScope = clazz.isAnnotationPresent(Scope.class);
            Scope scope = clazz.getAnnotation(Scope.class);
            if (isExistsAnnotationComponent) {
                // 附加：判断是否实现了BeanPostProcessor接口
                boolean isImplementsBeanPostProcessor = BeanPostProcessor.class.isAssignableFrom(clazz);
                if (isImplementsBeanPostProcessor) {
                    try {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

                // 2.7 如果有Component注解，则创建BeanDefinition对象
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setType(clazz);
                // 2.8 判断是否是单例
                boolean isSingleton = !isExistsAnnotationScope || ("".equals(scope.value()) || SCOPE_SINGLETON.equals(scope.value()));
                if (isSingleton) {
                    // 单例
                    beanDefinition.setScope(SCOPE_SINGLETON);
                } else {
                    beanDefinition.setScope(scope.value());
                }
                // 2.9 存储BeanDefinitionMap中
                Component componentAnnotation = clazz.getAnnotation(Component.class);
                String beanName = componentAnnotation.value();
                // 2.10 如果没有指定beanName，则默认使用类名首字母小写
                if ("".equals(beanName)) {
                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    /**
     * 创建bean
     *
     * @param beanDefinition bean定义
     * @return bean
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getType();
        try {
            Object instance = clazz.getConstructor().newInstance();
            // 依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                // 判断是否有Autowired注解
                boolean isExistsAnnotationAutowired = field.isAnnotationPresent(Autowired.class);
                if (isExistsAnnotationAutowired) {
                    // 省略通过类型注入，直接通过名称注入
                    String fieldName = field.getName();
                    Object bean = getBean(fieldName);
                    // 反射注入
                    field.setAccessible(true);
                    field.set(instance, bean);
                }

                // 判断是否实现了BeanNameAware接口
                if (instance instanceof BeanNameAware) {
                    ((BeanNameAware) instance).setBeanName(beanName);
                }

                // 执行BeanPostProcessor的前置方法
                for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                    instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
                }

                // 判断是否实现InitializingBean
                if (instance instanceof InitializingBean) {
                    ((InitializingBean) instance).afterPropertiesSet();
                }

                // 执行BeanPostProcessor的后置方法
                for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                    instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取@ComponentScan注解的value值(扫包路径)
     *
     * @param configClass 配置类
     * @return 扫包路径
     */
    private static String getComponentScanPath(Class configClass) {
        boolean annotationPresent = configClass.isAnnotationPresent(ComponentScan.class);
        if (!annotationPresent) {
            throw new RuntimeException("配置类上没有@ComponentScan注解");
        }
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value().replace(".", "/");
        return path;
    }
}
