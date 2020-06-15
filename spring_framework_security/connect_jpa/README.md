# 스프링 시큐리티 커스터마이징: JPA 연동

> Account

```java
@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String password;

    @Column(unique = true)
    private String username;

    private String role;

    public void encodePassword() {
        this.password = "{noop}" + this.password;
    }
}
```

## repository

> AccountRepository

```java
@Repository
public interface AccountRespository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
}
```

## Service

> AccountService

```java
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRespository accountRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRespository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createNew(Account account) {
        account.encodePassword();
        return this.accountRespository.save(account);
    }
}
```


## controller

> AccountController

```java
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{role}/{username}/{password}")
    public Account createAccount(@ModelAttribute Account account){
        return accountService.createNew(account);
    }

}
```

> SampleController

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
