# SrpingApplication 1부

>src/main/resource

Banner.txt|jpg|png를 추가하여 Banner를 바꿔줄 수 있다.

```
=========================================
DongWoo SpringBoot ${spring-boot.version}
=========================================
```
`${spring-boot.version}`를 이용하여 스프링 버전을 출력할 수 있다.


### 결과
```shell
=========================================
DongWoo SpringBoot 2.2.5.RELEASE
=========================================
```

## Banner를 사용안하도록 끄기
```java
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        // Banner를 사용 안하도록 끈다.
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
```


## Banner를 코딩으로 구현하기

### SpringApplication의 setBanner를 이용한 방법
```java
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.println("======================");
                out.println("dong woo");
                out.println("======================");
            }
        });
        // Banner를 사용 안하도록 끈다.
//        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
```

### SpringApplicationBuilder()를 이용하여 구현하는 방법
```java
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(DemoApplication.class)
                .run(args);
    }

}
```