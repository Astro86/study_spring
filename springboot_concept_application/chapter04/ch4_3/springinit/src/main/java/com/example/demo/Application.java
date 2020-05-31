package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
//        MVC가 있으면 기본적으로 SERVLET으로 작동한다.
//        app.setWebApplicationType(WebApplicationType.SERVLET);
//        SERVLET이 없고 Spring WebFlux가 들어있으면 REACTIVE로 작동한다.
//        app.setWebApplicationType(WebApplicationType.REACTIVE);
//        둘다 없으면 NONE으로 작동한다.
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
