# repository 만들기

> BoardRepository

```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
```

> UserRepository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
```