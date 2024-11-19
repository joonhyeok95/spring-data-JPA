package jpabook.jpashop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.service.query.OrderDto;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Repository
// @RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /*
     * 검색 구현
     */
    public List<Order> findAll_OLD(OrderSearch orderSearch) {
        // JPQL 에서의 join
        return em.createQuery("select o from Order o join o.member m", Order.class).getResultList();
        // 검색 조건이 모두 있을 때
        // return em.createQuery("select o from Order o join o.member m" +
        // " where o.status = :status " +
        // " and m.name like :name", Order.class)
        // .setParameter("status", orderSearch.getOrderStatus())
        // .setParameter("name", orderSearch.getMemberName())
        //// .setFirstResult(100) // 페이징 시
        // .setMaxResults(1000) // 최대 1000건
        // .getResultList();
        // 우리의 목적은 null 일 때의 동적 쿼리를 구현해야함.

    }

    public List<OrderDto> findAll(OrderSearch orderSearch) {

        // QueryDSL 해보기
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        List<Order> orders = query.select(order).from(order).join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000).fetch();
        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return QOrder.order.status.eq(orderStatus);
    }

    private BooleanExpression nameLike(String nameCond) {
        if (!StringUtils.hasText(nameCond)) {
            return null;
        }
        return QMember.member.name.like("%" + nameCond + "%");
    }

    /*
     * JPA Criteria (표준이나 권장하지 않음) Why? 유지보수성이 너무 떨어짐. SQL을 떠올릴 수 없는 복잡한 구조.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    // 성능 개선 fetch join
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" + " join fetch o.member m" + " join fetch o.delivery d",
                Order.class).getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select o from Order o" + " join fetch o.member m" + " join fetch o.delivery d"
                        + " join fetch o.orderItems oi" + " join fetch oi.item i",
                Order.class).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em
                .createQuery("select o from Order o" + " join fetch o.member m"
                        + " join fetch o.delivery d", Order.class)
                .setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) "
                        + " from Order o" + " join o.member m" + " join o.delivery d",
                OrderSimpleQueryDto.class).getResultList();
    }


}
