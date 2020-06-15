# 스프링 시큐리티 커스터마이징: PasswordEncoder

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

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
```


> AccountService

```java
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRespository accountRespository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        account.encodePassword(passwordEncoder);
        return this.accountRespository.save(account);
    }
}
```


> SecurityApplication

```java
@SpringBootApplication
@EnableJpaAuditing
public class SecurityApplication {



    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }


    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}
```