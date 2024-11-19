package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
	
	private final EntityManager em;
	// case 1. 1+N 문제가 있는 상황
	public List<OrderQueryDto> findOrderQueryDtos(){
		List<OrderQueryDto> result = findOrders(); // Query 1번 -> N개
		
		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // Query N 번
			o.setOrderItems(orderItems);
		});
		return result;
	}
	// case 2. 퀄렉션 조회 최적화 - Query 2번 나감 그리고 메모리에서 값을 매칭해서 세팅함
	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();
		
		List<Long> orderIds = result.stream().map(o -> o.getOrderId())
											.collect(Collectors.toList());
		
		List<OrderItemQueryDto> orderItems = em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
				" from OrderItem oi" +
				" join oi.item i" +
				" where oi.order.id in :orderIds", OrderItemQueryDto.class)
		.setParameter("orderIds", orderIds)
		.getResultList();
		// orderItems 최적화(Map 변경:매칭 성능 향상(O(1)))
		Map<Long, List<OrderItemQueryDto>> orderItemsMap = orderItems.stream()
			.collect(Collectors.groupingBy(OrderItemQueryDto -> OrderItemQueryDto.getOrderId())); // 그룹화해서 맵으로 변경할 수 있다 key:orderId
		result.forEach(o -> o.setOrderItems(orderItemsMap.get(o.getOrderId())));
		
		return result;
	}
	
	private List<OrderItemQueryDto> findOrderItems(Long orderId){
		return em.createQuery(
						"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
						" from OrderItem oi" +
						" join oi.item i" +
						" where oi.order.id = :orderId", OrderItemQueryDto.class)
				.setParameter("orderId", orderId)
				.getResultList();
	}

	private List<OrderQueryDto> findOrders(){
		return em.createQuery(
						"select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
						" from Order o" +
						" join o.member m" +
						" join o.delivery d", OrderQueryDto.class)
				.getResultList();
		
	}
	// V6 : order 페이징 불가
	public List<OrderQueryDto> findAllByDto_flat() {
		List<OrderFlatDto> flats =  em.createQuery(
						"select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
						" from Order o" +
						" join o.member m" +
						" join o.delivery d" +
						" join o.orderItems oi" +
						" join oi.item i", OrderFlatDto.class)
				.getResultList();
		// api 규격에 맞게 메모리에서 돌려준다.
		return flats.stream()
				.collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
						Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
				)).entrySet().stream()
				.map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
				.collect(Collectors.toList());
//	    return flats.stream()
//	            .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
//	            		Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
//	            )).entrySet().stream()
//	            .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
//	            .collect(Collectors.toList());

//		return null;
	}
}
