# 스프링 웹 MVC 4부 : 정적 리소스 지원

정적 리소스 맵핑 “/**”
- 기본 리소스 위치
  - classpath:/static 
  - classpath:/public 
  - classpath:/resources/ 
  - classpath:/META-INF/resources 
    - 예) “/hello.html” => /static/hello.html 
  - spring.mvc.static-path-pattern: 맵핑 설정 변경 가능 
  - spring.mvc.static-locations: 리소스 찾을 위치 변경 가능

- Last-Modified 헤더를 보고 304 응답을 보냄. 
- ResourceHttpRequestHandler가 처리함.
  - WebMvcConfigurer의 addRersourceHandlers로 커스터마이징 할 수 있음

## spring.mvc.static-path-pattern을 이용할 경우
```properties
spring.mvc.static-path-pattern = /static/**
```

> http://localhost:8080/static/hello.html

기본 경로 앞에 static을 붙여서 요청을 해줘야 한다.

## WebMvcConfigurer의 addResourceHandlers를 이용하는 경우
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    // ResourceHandler를 추가하자.
    // 기존에 재공하는 ResourceHandler는 유지하면서
    // 원하는 resource handler만 추가할 수 있다.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/")
                .setCachePeriod(20);
    }
}
```