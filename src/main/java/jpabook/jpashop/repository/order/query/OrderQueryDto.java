package jpabook.jpashop.repository.order.query;

import java.time.LocalDateTime;
import java.util.List;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "orderId") // 컬렉터 그룹바이시 묶어주는 기준
public class OrderQueryDto {
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	private List<OrderItemQueryDto> orderItems;
	
	public OrderQueryDto (Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
	}
	// V6 용도
	public OrderQueryDto(Long orderId2, String name2, LocalDateTime orderDate2, OrderStatus orderStatus2, Address address2, List<OrderItemQueryDto> orderItems) {
		this.orderId = orderId2;
		this.name = name2;
		this.orderDate = orderDate2;
		this.orderStatus = orderStatus2;
		this.address = address2;
		this.orderItems = orderItems;
	}
}
