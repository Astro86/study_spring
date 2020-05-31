package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan
//@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        // SpringApplication.run()을 바로 사용하게 되면 웹 어플리케이션 형태로 작동을 한다.
        // EnableAutoConfiguration 어노테이션 없이는 웹 어플리케이션으로 만드는 것이 불가능 하므로
        // 바로 사용할 수 없다.
        // SpringApplication.run(Application.class, args);

        // SpringApplication 인스턴스를 생성하면 커스터마이징 해서 사용이 가능하다.
        SpringApplication application = new SpringApplication(Application.class);
        // wegApplication만드는 설정을 꺼준다.
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);

    }
}
