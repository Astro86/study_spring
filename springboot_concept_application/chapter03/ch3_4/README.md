# 자동 설정 만들기 1부
- Xxx-Spring-Boot-Autoconfigure 모듈: 자동 설정  
- Xxx-Spring-Boot-Starter 모듈: 필요한 의존성 정의  

그냥 하나로 만들고 싶을 때는?
Xxx-Spring-Boot-Starter

## spring-boot-starter 만들기
> 프로젝트 명 : maplespringbootstarter

> ArtifactId : maple-spring-boot-starter

## 의존성 추가

> maplespringbootstarter 프로젝트

```xml
<dependencies>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure-processor</artifactId>
      <optional>true</optional>
  </dependency>
</dependencies>

<dependencyManagement>
  <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-dependencies</artifactId>
          <version>2.0.3.RELEASE</version>
          <type>pom</type>
          <scope>import</scope>
      </dependency>
  </dependencies>
</dependencyManagement>
```

### Holoman.java

> maplespringbootstarter 프로젝트

```java
package com.example;

public class Holoman {

    String name;
    int howLong;

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

    @Override
    public String toString() {
        return "Holoman{" +
                "name='" + name + '\'' +
                ", howLong=" + howLong +
                '}';
    }
}
```


### HolomanConfiguration

>  maplespringbootstarter 프로젝트

```java
@Configuration
public class HolomanConfiguration {

    @Bean
    public Holoman holoman(){
        Holoman holoman = new Holoman();
        holoman.setHowLong(5);
        holoman.setName("maple");

        return holoman;
    }
}
```
Holoman 인스턴스를 반환하는 설정 파일을 만들었다.

### 자동 설정 파일 추가
> src/main/resource/위치에  `META-INF`폴더를 생성한다.
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.HolomanConfiguration
```
`META-INF`폴더에 `spring.factories`파일을 생성한 후 위 내용을 추가해준다.  
해당 프로젝트를 의존성에 추가한 프로젝트는 `EnableAutoConfiguration`이 HolomanConfiguration을 `scan`하여 `configuration`의 `Bean`을 추가해 준다.

### mvn install
<image src=./picture/mvn_install.png width = 250/>

install을 더블클릭하거나 
콘솔에가서 `mvn install`명령어를 실행하면 된다.
```shell
mvn install
```
프로젝트가 빌드되고 jar파일이 생성된다. 다른 maven 프로젝트에서 가져다 쓸 수 있도록 local maven저장소에  설치를 한다.


## 의존성 추가

> springbootgetttingstarted 프로젝트를 새로 생성

```xml
<groupId>org.example</groupId>
<artifactId>maple-spring-boot-starter</artifactId>
<version>1.0-SNAPSHOT</version>
```
pom.xml에 dependency를 추가한다.

<image src="picture/starter.png" width = 250>

maple-spring-boot-starter가 추가 된 것을 확인할 수 있다.

### HolomanRunner.java
> springbootgettingstarted 프로젝트
```java
@Component
public class HolomanRunner implements ApplicationRunner {

    // maple-spring-boot-starter에서 Holoman Bean을 가져와 의존성을 주입해준다.
    @Autowired
    Holoman holoman;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(holoman);
    }
}
```
Holoman Bean이 추가된 것을 확인하기 위한 코드  
해당 프로젝트에는 Holoman에 관한 Configuration이 없는 상태이므로 Holoman Bean이 추가되었는지 해당 코드를 통해 확인 할 수 있다.

## 결과
```shell
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.5.RELEASE)

2020-03-15 20:29:06.744  INFO 53354 --- [           main] com.example.Application                  : Starting Application on yangdong-uui-MacBookPro.local with PID 53354 (/Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_4/springbootgettingstarted/target/classes started by dongwoo-yang in /Users/dongwoo-yang/dev/study/spring/everyday_spring/chapter3/ch3_4/springbootgettingstarted)
2020-03-15 20:29:06.749  INFO 53354 --- [           main] com.example.Application                  : No active profile set, falling back to default profiles: default
2020-03-15 20:29:07.495  INFO 53354 --- [           main] com.example.Application                  : Started Application in 1.395 seconds (JVM running for 2.439)
Holoman{name='maple', howLong=5}

Process finished with exit code 0
```
`Holoman{name='maple', howLong=5}`을 통해 정상적으로 작동함을 알 수 있다.

## 만약 프로젝트에 같은 같은 Bean이 있는 경우

> springbootgettingstarted 프로젝트

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Bean
    public Holoman holoman(){
        Holoman holoman = new Holoman();
        holoman.setName("동장군");
        holoman.setHowLong(60);
        return holoman;
    }
}
```

해당 프로젝트의 Bean이 무시가 된다. `ComponentScan`을 통해 먼저 Bean이 등록이 되고 `EnableAutoConfiguration`을 통해 Bean이 등록이 될때 해당 Bean을 덮어쓰게 된다.

### 다른 결과
```shell
***************************
APPLICATION FAILED TO START
***************************

Description:

The bean 'holoman', defined in class path resource [com/example/HolomanConfiguration.class], could not be registered. A bean with that name has already been defined in com.example.Application and overriding is disabled.

Action:

Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true


Process finished with exit code 1

```
스프링 부트 2.2로 올라가면서 Bean 오버라이딩이 막혀서 에러 메시지가 뜨게 된다. 만약 Bean 오버라이딩을 하려고 하면 `spring.main.allow-bean-definition-overriding=true` 설정을 추가하면 Bean 오버라이딩이 가능해 진다.