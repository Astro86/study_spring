# JPA

## 비영속
> JPA와 전혀 관계가 없는 상태(객체만 생성)

## 영속
> Entity Manager에 persist로 Entity를 넣은 상태

영속상태가 된다고 해서 바로 Database에 query가 날라가는 것이 아니다. transction을 commit하게 되면 DB에 query가 날라가게 된다.

- persist : 영속성 context에 저장
- detech : 영속성 context에서 지움

## 영속성 context

> application과 database 사이에는 중간 계층이 존재한다.
> Entity를 영구적으로 저장하는 환경
 
장점 : buffering이나 Caching을 할 수 있다.

영속성  Context는 내부에 1차 cache가 존재한다.

|   @Id   | Entity |
| :-----: | :----: |
| Member1 | member |

- @Id : Key
- Entity : 객체

만일 1차 캐시에 data가 없으면 
1. DB에서 조회
2. 1차 Cache에 데이터를 저장(있으면)

### EntityManager

Entity Manager는 transaction 단위로 만들고 DataBase transaction이 끝나면 같이 종료 시킨다.

EntityManager.persist(entity) => 사실 DB에 저장하는 것이 아니다. 영속성 context를 통해 Entity를 영속화 한다.

> 사실 DB에 저장하는 것이 아니라. Entity를 영속성 context라는데 저장한다.

Entity Manager와 영속성 Context가 1:1

## Entity 생명주기
1. 비영속
2. 영속
3. 준영속
4. 삭제