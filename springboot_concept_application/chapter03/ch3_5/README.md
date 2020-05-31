# 자동 설정 만들기 2부

## EnableAutoConfiguration이 불러오는 같은 Bean 무시하기

> maplespringbootstarter 프로젝트

```java
@Configuration
public class HolomanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Holoman holoman(){
        Holoman holoman = new Holoman();
        holoman.setHowLong(5);
        holoman.setName("maple");

        return holoman;
    }
}
```

`@ConditionalOnMissingBean`을 사용하면 해당 `Bean`이 없는 경우에만 `EnableAutoConfiguration`을 통해 `Bean`이 추가 된다.

### 결과

```shell
2020-03-15 21:31:49.936  INFO 67228 --- [           main] com.example.Application                  : Starting Application on yangdong-uui-MacBookPro.local with PID 67228 (/Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_5/springbootgettingstarted/target/classes started by dongwoo-yang in /Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_5/springbootgettingstarted)
2020-03-15 21:31:49.938  INFO 67228 --- [           main] com.example.Application                  : No active profile set, falling back to default profiles: default
2020-03-15 21:31:50.581  INFO 67228 --- [           main] com.example.Application                  : Started Application in 0.937 seconds (JVM running for 1.598)
Holoman{name='동장군', howLong=60}

Process finished with exit code 0
```

`springbootgetttingstarted`프로젝트의 `Bean`이 출력됨을 확인할 수 있다.

## application.properties를 이용한 방법

> springbootgetttingstarted 프로젝트 src/resource에 `application.properties`파일을 생성

```
holoman.name = 아 집에 가고 싶다.
holoman.how-long = 10000
```

`application.propertie`에 해당 내용을 추가

### 의존성 추가

> maplespringbootstarter 프로젝트

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-configuration-processor</artifactId>
   <optional>true</optional>
</dependency>
```

`@ConfigurationProperties`를 사용하기 위해서는 해당 의존성을 추가해주어야 한다.

### HolomanProperties

> maplespringbootstarter 프로젝트

```java
@ConfigurationProperties("holoman")
public class HolomanProperties {
    private String name;
    private int howLong;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHowLong() {
        return howLong;
    }

    public void setHowLong(int howLong) {
        this.howLong = howLong;
    }
}
```

### HolomanConfiguration 수정

> maplespringbootstarter 프로젝트

```java
@Configuration
@EnableConfigurationProperties(HolomanProperties.class)
public class HolomanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Holoman holoman(HolomanProperties properties){
        Holoman holoman = new Holoman();
        holoman.setHowLong(properties.getHowLong());
        holoman.setName(properties.getName());

        return holoman;
    }
}
```

@EnableConfigurationProperties를 추가
Bean을 만들 때 HolomanProperties를 참조해서 Bean을 생성한다.  
springbootgetttingstarted의 application.properties를 가져다가 Bean을 생성한다.

> springbootgetttingstarted내의 holoman Bean을 없애주어야 한다.... 오류 잡느라 시간 오래 걸렸다.

### 결과

```java
2020-03-15 22:07:53.804  INFO 70087 --- [           main] com.example.Application                  : Starting Application on yangdong-uui-MacBookPro.local with PID 70087 (/Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_5/springbootgettingstarted/target/classes started by dongwoo-yang in /Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_5/springbootgettingstarted)
2020-03-15 22:07:53.808  INFO 70087 --- [           main] com.example.Application                  : No active profile set, falling back to default profiles: default
2020-03-15 22:07:54.628  INFO 70087 --- [           main] com.example.Application                  : Started Application in 1.148 seconds (JVM running for 1.631)
Holoman{name='A... I want to go home', howLong=10000}

Process finished with exit code 0
```
