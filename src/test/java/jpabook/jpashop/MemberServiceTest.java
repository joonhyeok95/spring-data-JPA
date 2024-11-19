package jpabook.jpashop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;


/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService; // 다른애들이 참조할게 없으니 간단하게 주입
    @Autowired
    MemberRepository memberRepository;

    // @Autowired EntityManager em; // 실제 db 반영 case 2
    @Test
    // @Rollback(false) // 실제 db 반영 case 1
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("lim");

        // when
        Long saveId = memberService.join(member);
        // em.flush();

        // then
        assertEquals(member, memberRepository.findOne(saveId));

    }

    // @Test(expected = IllegalStateException.class) // Junit4
    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("lim");
        Member member2 = new Member();
        member2.setName("lim");

        // when
        // Junit 4
        // memberService.join(member1);
        // memberService.join(member2);
        // Junit 5
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });


        // then
        // fail("예외 발생..");
    }


    // @Test
    // @Transactional
    // @Rollback(false) // Test 자동 롤백 끄기
    // public void testMember() throws Exception {
    // // TODO Auto-generated method stub
    //// Member member = new Member();
    //// member.setUsername("joonhyeok");
    ////
    //// Long saveId = memberRepository.save(member);
    //// Member findMember = memberRepository.find(saveId);
    ////
    //// Assertions.assertThat(findMember.getId()).isEqualTo(member.getId()); // 1. 실제 쿼리가 날라 감
    //// Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername()); // 2. 캐시에서
    // 뽑아냄
    //// Assertions.assertThat(findMember).isEqualTo(member); // 3. 같은 영속성 컨텍스트에선 id 가 같으면 같다.
    // }
}
