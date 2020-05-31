package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {
    @Autowired
    DongwooProperties dongwooProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=================");
        System.out.println(dongwooProperties.getName());
        System.out.println(dongwooProperties.getAge());
        System.out.println("=================");
    }
}
