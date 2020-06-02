# 회원 도메인 개발

## 구현 기능
- 회원 등록
- 회원 목록 조회

## 순서
- 회원 엔티티 코드 다시보기
- 회원 repository 개발
- 회원 서비스 개발
- 회원 기능 테스트

## 코드
> 회원 repository 개발

```java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
```

MemberRepository

> 데이터 베이스에 직접 접근하기 위한 객체

| field명    | 설명                                        |
| ---------- | ------------------------------------------- |
| save       | Member 객체를 영속화 한다.                  |
| findOne    | id를 이용해 해당되는 Member객체를 가져온다. |
| findAll    | 모든 Member객체를 가져온다.                 |
| findByName | 이름을 이용해 해당되는 Member를 가져온다.   |

> 회원 서비스 개발

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
```

### MemberService
| field명                 | 설명                                                                                                         |
| ----------------------- | ------------------------------------------------------------------------------------------------------------ |
| memberRepository        | member 테이블에 접근하기 위한 객체                                                                           |
| join                    | 중복여부를 검사한 후 member테이블에 Member를 추가해주는 역할                                                 |
| validateDuplicatemember | member 테이블이 해당 member를 가지고 있는지 확인을 해주는 역할을 한다. 만약 있는 경우에는 에러를 출력해준다. |
| findMembers             | 전체 회원을 조회해주는 역할을 한다.                                                                          |
| findOne                 | 해당 id를 가지고 있는 회원을 찾아준다.                                                                       |



## 테스트 코드 작성

> 회원가입 테스트 코드

```java
package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long saveId = memberService.join(member);
        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다.");

    }
}
```