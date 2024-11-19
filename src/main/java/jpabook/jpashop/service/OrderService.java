package jpabook.jpashop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepositoryOld memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        // Q. 원래 deliveryRepository/orderItem 도 필요할텐데..?
        // A. Order 에 cascade 옵션 때문에 자동으로 주문 저장까지 완료됨
        // 언제써야할까? -> lifecycle에 대해 동일하게 관리를 할때
        // -> private 다른곳에서 참조하지 못하는 것
        // -> 다른곳에서 참조한다면 cscade 옵션을 쓰면안된다(repostory 사용)

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // OrderItem orderItem = new OrderItem(); //방식은 막아라

        // 주문생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
        // mybatis 를 썻다면 밖에서 주문취소->재고업->상태바꿈... 의 로직을 했어야 됬는데
        // JPA는 변경 포인트들을 감지하여 데이터베이스에 업데이트 쿼리가 날라간다.
        // 위 cancel() 하나로 -> OrderState.CANCEL, Item Stock Quantity update ...
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        // return orderRepository.findAll(orderSearch);
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
