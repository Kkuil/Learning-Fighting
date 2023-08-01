package com.kkuil;

import com.spring.KkuilApplicationContext;
import com.spring.UserInterface;

public class test {
    public static void main(String[] args) throws ClassNotFoundException {
        KkuilApplicationContext context = new KkuilApplicationContext(AppConfig.class);

        UserInterface userService = (UserInterface) context.getBean("userService");
        userService.test();
    }
}
