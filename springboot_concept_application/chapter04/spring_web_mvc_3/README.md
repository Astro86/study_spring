# 스프링 웹 MVC 3부 ViewResolve

들어오는 요청의 accept해더에 따라 응답이 달라진다.
클라이언트가 어떠한 타입의 응답을 원하는지 서버에 알려주면 accept해더에 따라 응답이 달라진다.

accept해더를 제공하지 않는 요청들은 format이라는 매게변수를 사용한다.

## XML로 받았을 경우

> test/java/com.example.demo/user/UserControllerTest.java

```java
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    public void CreateUser_XML() throws Exception {
        String userJson = "{\"username\":\"dongwoo\", \"password\" : \"123\"}";
        // 요청을 만드는 단
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .accept(MediaType.APPLICATION_XML)
                .content(userJson))
                    .andExpect(status().isOk())
                    .andExpect(xpath("User/username")
                            .string("dongwoo"))
                    .andExpect(xpath("User/password")
                            .string("123"));
    }
}
```

## 결과

```
Resolved Exception:
    Type = org.springframework.web.HttpMediaTypeNotAcceptableException
```

미디어 타입을 처리할 Http converter가 존재하지 않는다는 의미이다.

## 의존성 주입

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.6</version>
</dependency>
```

