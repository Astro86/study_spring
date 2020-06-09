# 내가 공부한 스프링

- [spring 4](spring4/README.md)
- [처음 시작하는 스프링 부트2](springboot2/README.md)
- [스프링부트 프로그래밍 입문](springboot_tutorial/README.md)
- []

## 김영환 교수님의 스프링 & JPA 강좌
- [자바 ORM 표준 JPA 프로그래밍 - 기본편](JPA/README.md)
- [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](practice_JPA_1/README.md)

## 백기선 교수님의 스프링 강좌
- [스프링부트 개념과 활용](springboot_concept_application/README.md)

## 자바 Bean? 스프링 Bean? 이란???

## Entity vs DTO vs VO

> Entity

- Entity클래스는 DB의 테이블내에 존재하는 컬럼만을 필드로 가지는 클래스 
- 엔티티 클래스는 상속을 받거나 구현체여서는 안되며, 테이블내에 존재하지 않는 컬럼을 가져서도 안된다.

> DTO (Data Transfer Object)

- 계층간의 데이터 전송을 위해 필요한 객체이다.
- Setter/Getter로 이루어져 있다. 

데이터 전송 객체라는 의미를 가지고 있습니다. DTO는 주로 비동기 처리를 할 때 사용합니다. 비동기 처리에서도 JSON데이터 타입으로 변환해야 하는 경우, Spring Boot에서 Jackson 라이브러리를 제공하는데, Jackson은 ObjectMapper를 사용해서 별다른 처리 없이도 객체를 JSON타입으로 변환시켜 줍니다.

> VO

- 값으로 취급할 객체
- 변하지 않는 값을 가지는 객체(불변성), 내부 속성을 변경할 수 없다.
- 값이 같다면 동등한 객체
- VO의 핵심 역할은 equels()와 hashcode()를 오버라이딩 하는 것입니다.(참조객체의 동등을 표현하기 위해서)

### 동일과 동등

- 동일 : 참조하고 있는 객체가 같다는 것을 의미한다.(== 연산자를 사용)
- 동등 : 객체가 참조하는 대상의 속성 값 혹은 동등하게 하는 조건이 같음을 의미한다. (equals라는 연산자를 사용)

| DTO                                    | VO                                   |
| -------------------------------------- | ------------------------------------ |
| 값이 변할 수 있다.                     | 값이 변하지 않는다.                  |
| 레이어와 레이어 사이에서 사용한다.     | 모든 레이어에서 사용이 가능하다.     |
| DTO dto1(1) != DTO dto(1)              | VO vo1(1) == vO vo2(1)               |
| 데이터 접근 외의 기능을 가지지 않는다. | 특정한 비즈니스 로직을 가질 수 있다. |

## domain에서 사용하는 어노테이션


## JpaRepository

![](images/jpa_repository_inheritence.png)