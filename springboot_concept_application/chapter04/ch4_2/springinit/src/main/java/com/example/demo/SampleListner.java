package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SampleListner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(args).forEach(System.out::println);
        System.out.println(args);
    }
}


//@Component
//public class SampleListner implements ApplicationRunner {
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("foo: "+args.containsOption("foo"));
//        System.out.println("bar: "+args.containsOption("bar"));
//    }
//}


//@Component
//public class SampleListner{
//
//    // Bean의 생성자가 1개이고
//    // 그 생성자의 parameter가 Bean일 경우
//    // 그 Bean을 Spring boot가 알아서 주입해준다.
//    public SampleListner(ApplicationArguments arguments){
//        System.out.println("foo: " + arguments.containsOption("foo"));
//        System.out.println("bar: " + arguments.containsOption("bar"));
//    }
//}


//@Component
//public class SampleListner implements ApplicationListener<ApplicationStartedEvent>{
//    @Override
//    public void onApplicationEvent(ApplicationStartedEvent event) {
//        System.out.println("============");
//        System.out.println("Started");
//        System.out.println("============");
//    }
//}


//public class SampleListner implements ApplicationListener<ApplicationStartingEvent> {
//    @Override
//    public void onApplicationEvent(ApplicationStartingEvent event) {
//        System.out.println("=======================");
//        System.out.println("Application is starting");
//        System.out.println("=======================");
//    }
//}
