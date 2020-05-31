# 외부 설정 1부

## application.properties

key-value형태로 값을 정의 하면 애플리케이션에서 참조해서 사용한다.

> application.properties

```
dongwoo.name = dongwoo
```

> src/main/java/com.example/SampleRunner.class

```java
@Component
public class SampleRunner implements ApplicationRunner {
    @Value("${dongwoo.name}")
    private String name;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=================");
        System.out.println(name);
        System.out.println("=================");
    }
}
```

### 결과
```shell
=================
dongwoo
=================
```

## 프로퍼티 우선순위

1. 유저 홈 디렉토리에 있는 spring-boot-dev-tools.properties
2. 테스트에 있는 @TestPropertySource
3. @SpringBootTest 애노테이션의 properties 애트리뷰트
4. 커맨드 라인 아규먼트
5. SPRING_APPLICATION_JSON (환경 변수 또는 시스템 프로티) 에 들어있는 프로퍼티
6. ServletConfig 파라미터
7. ServletContext 파라미터
8. java:comp/env JNDI 애트리뷰트
9. System.getProperties() 자바 시스템 프로퍼티
10. OS 환경 변수
11. RandomValuePropertySource
12. JAR 밖에 있는 특정 프로파일용 application properties
13. JAR 안에 있는 특정 프로파일용 application properties
14. JAR 밖에 있는 application properties
15. JAR 안에 있는 application properties
16. @PropertySource
17. 기본 프로퍼티 (SpringApplication.setDefaultProperties)

프로퍼티 우선순위를 이용하여 overriding할 수 있다.

```shell
java -jar demo-0.0.1-SNAPSHOT.jar --dongwoo.name=RIN
```
커맨드 라인 아규먼트로 실행을 하게 되면 application properties가 무시되고 커맨드 라인 아규먼트로 덮어쓰기가 된다.


## 테스트를 이용한 프로퍼티 번경
```java
@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

    // Environment는 spring에 들어있는 것을 가져와야 한다.
    @Autowired
    Environment environment;

    @Test
    void contextLoads() {
        assertThat(environment.getProperty("dongwoo.name"))
                .isEqualTo("dongwoo");
    }

} 
```