# SpringApplication 2부


## Application Event 등록하기

> src/main/java/SampleListner.class

### ApplicationStartingEvent

```java
@Component
public class SampleListner implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("=======================");
        System.out.println("Application is starting");
        System.out.println("=======================");
    }
}
```

> src/main/java/Application.class

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        // 이벤트 리스너를 동록한다.
        app.addListeners(new SampleListner());
        app.run(args);
    }

}
```

### ApplicationStartedEvent


> src/main/java/SampleListener.class

```java
@Component
public class SampleListner implements ApplicationListener<ApplicationStartedEvent>{
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("============");
        System.out.println("Started");
        System.out.println("============");
    }
}
```

> src/main/java/Application.class

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
```

## Web Application 타입 설정
```java
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
```

## 어플리케이션 아규먼트 사용하기

> Edit Configuration

VM options : `-Dfoo`
Program arguments : `--bar`를 설정해 준다.

```java
@Component
public class SampleListner{

    // Bean의 생성자가 1개이고
    // 그 생성자의 parameter가 Bean일 경우
    // 그 Bean을 Spring boot가 알아서 주입해준다.
    public SampleListner(ApplicationArguments arguments){
        System.out.println("foo: " + arguments.containsOption("foo"));
        System.out.println("bar: " + arguments.containsOption("bar"));
    }
}
```

### 결과

```shell
foo: false
bar: true
```


### 쉘 스크립트로 실행해보기
```shell
mvn clean package
mvn package
cd target
# java -jar [.jar파일] [option]
java -jar demo-0.0.1-SNAPSHOT.jar -Dfoo --bar
```

## 애플리케이션 실행하나 뒤 뭔가 실행하고 싶을 경우

### ApplicationRunner

> src/main/java/SampleListner.class

```java
@Component
public class SampleListner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("foo: "+args.containsOption("foo"));
        System.out.println("bar: "+args.containsOption("bar"));
    }
}
```
### 결과
```shell
foo: false
bar: true
```

### CommandLineRunner

> src/main/java/SampleListner.class

```java
@Component
public class SampleListner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(args).forEach(System.out::println);
        System.out.println(args);
    }
}
```

### 결과
```shell
--bar
```