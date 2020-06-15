# 스프링 시큐리티 설정하기

```java
@Controller
public class SampleController {

    @GetMapping("/")
    public String index(Model model, Principal principal){

        if(principal == null){
            model.addAttribute("message", "Hello Spring Security");
        }else{
            model.addAttribute("message", "Hello " + principal.getName());
        }
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model){
        model.addAttribute("message", "Hello Spring Security");
        return "info";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("message", "Hello " + principal.getName());
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal){
        model.addAttribute("message", "Hello Admin, " + principal.getName());
        return "admin";
    }
}
```

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and();

        http.formLogin();
        http.httpBasic();
    }
}
```

## 템플릿

> index

```html
<!DOCTYPE html>
<html lang="en"
    xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1 th:text="${message}">Hello</h1>
</body>
</html>
```

>info

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Info</title>
</head>
<body>
<h1 th:text="${message}">Info</h1>
</body>
</html>
```

> dashboard

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Dash Board</title>
</head>
<body>
<h1 th:text="${message}">Dashboard</h1>
</body>
</html>
```

> admin

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.orgh">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
</head>
<body>
<h1 th:text="${message}">Admin</h1>
</body>
</html>
```

