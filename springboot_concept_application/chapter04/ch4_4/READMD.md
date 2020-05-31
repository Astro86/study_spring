# 외부 설정 2부

## 같은 이름을 가진 property를 하나로 묶어주기

### 타입-세이프 프로퍼티

> src/main/resource/application.properties

```
dongwoo.name = dongwoo
dongwoo.age = ${random.int(0,100)}
dongwoo.fulName = ${dongwoo.name} Yang
```

> src/main/java/com.example/DongwooProperties.class

```java
@ConfigurationProperties("dongwoo")
public class DongwooProperties {

    private String name;
    private int age;
    private String fullName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
```

> pom.sml

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>
```

> src/main/java/com.example/SampleRunner.class

```java
@Component
public class SampleRunner implements ApplicationRunner {
    @Autowired
    DongwooProperties dongwooProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=================");
        System.out.println(dongwooProperties.getName());
        System.out.println(dongwooProperties.getAge());
        System.out.println("=================");
    }
}
```

### 결과

```shell
=================
dongwoo
86
=================
```
