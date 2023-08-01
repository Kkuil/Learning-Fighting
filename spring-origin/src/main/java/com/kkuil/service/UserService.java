package com.kkuil.service;

import com.kkuil.annotation.KkuilValue;
import com.spring.BeanNameAware;
import com.spring.UserInterface;
import com.spring.annotation.Autowired;
import com.spring.annotation.Component;

@Component("userService")
public class UserService implements UserInterface, BeanNameAware {

    @Autowired
    private OrderService orderService;

    @KkuilValue("kkuil")
    private String name;

    private String beanName;

    @Override
    public void test() {
        System.out.println(name);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
