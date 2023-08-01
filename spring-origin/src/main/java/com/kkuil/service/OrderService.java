package com.kkuil.service;

import com.spring.annotation.Component;
import com.spring.annotation.Scope;

@Component("orderService")
@Scope("prototype")
public class OrderService {

    public void test() {
        System.out.println("UserService.test()");
    }

}
