# 스프링 웹 MVC 2부 - HttpMessageConverters

HttpMessageConverters는 스프링 프레임워크에서 제공하는 인터페이스이고, spring web mvc의 일부분이다.

HTTP 요청 본문을 객체로 변경하거나, 객체를 HTTP 응답 본문으로 변경할 때 사용.
{“username”:”keesun”, “password”:”123”} <-> User

- @ReuqestBody
- @ResponseBody
와 같이 사용이 된다.

> main/java/com.example.demo/user/UserController.java

```java
@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    // 요청이 들어왔을 때 요청 내용을 객체로 받을 수 있다.
    // 이 때 HttpMessageConverter가 사용이 된다.
    // Request의 content-type이 Json일 경우 JSON Messgae converter가 사용이 돼서 User라는 객체로 변환시켜준다.
    // 반환하기 위해서는 User라는 객체를 Json Message Converter를 이용해 반환한다.
    // @RestController가 붙어있을 경우에는 @RequestBody를 생략해도 된다.
    // @RestController가 없는 곳에서 @RequestBody를 생략할 경우 view name resolver가 작동하여
    // return 하는 string과 같은 view를 찾아서 반환하게 된다.
    @PostMapping("/users/create")
    public User create(@RequestBody User user) {
        return user;
    }
}
```

> main/java/com.example.demo/user/User.java

```java
public class User {

    private Long id;
    private String username;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

## 테스트 코드 작성

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
    public void CreateUser_JSON() throws Exception {
        String userJson = "{\"username\":\"dongwoo\", \"password\" : \"123\"}";
        // 요청을 만드는 단
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username", is(equalTo("dongwoo"))))
                    .andExpect(jsonPath("$.password", is(equalTo("123"))));
    }
}
```
