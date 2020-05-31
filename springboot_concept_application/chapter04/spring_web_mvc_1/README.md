# 스프링 웹 MVC 1부


> src/test/java/com/example/demo/user/UserControllerTest

```java
@RunWith(SpringRunner.class)
@WebMvcTest(UserConroller.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect((content().string("hello")));
    }
}
```

> src/main/java/com/example/demo/user/UserController

```java
@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
```

## 스프링이 제공해주는 MVC외에 추가적인 설정을 할 경우

- @Configuration
- WebMvcConfiguration을 구현(implements)

## MVC를 재정의 할 경우

- @Configuration
- @EnableWebMvc