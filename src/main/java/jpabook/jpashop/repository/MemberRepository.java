package jpabook.jpashop.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 직접 Persistence Factory 를 주입하고 싶다면 @PersistenceContextUnit 사용.. 그럴일은 없을듯

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // JPQL 을 써야함
        // SQL 은 테이블을 대상으로 쿼리를 하지만
        // JPQL 는 엔티티 객체를 대상으로 조회한다.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByNames(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
    }

}
