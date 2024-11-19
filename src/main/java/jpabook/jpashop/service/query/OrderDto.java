package jpabook.jpashop.service.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

@Getter
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName(); // LAZY
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress(); // LAZY
        // orderItems = order.getOrderItems(); // 이러면 entity 이기 때문에 null 이 나온다.
        // order.getOrderItems().stream().forEach(o -> o.getItem()); // 이렇게 시도 할 수도있지만 결국 entity
        // 가 노출된다.
        orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem))
                .collect(Collectors.toList());
    }
}
