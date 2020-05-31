package com.example;


import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        // getApplicationContext()메소드를 이용하여 ApplicationContext를 얻는다.
        // ApplicationContext는 servlet ApplicationContext이기 때문 웹 서버를 알 수 있다.
        ServletWebServerApplicationContext applicationContext = event.getApplicationContext();
        // 얻어진 웹서버를 통해 포트를 알아낼 수 있다.
        System.out.println(applicationContext.getWebServer().getPort());
    }
}
