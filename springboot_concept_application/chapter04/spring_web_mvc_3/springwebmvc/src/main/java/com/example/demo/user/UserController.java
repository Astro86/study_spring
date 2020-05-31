package com.example.demo.user;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
