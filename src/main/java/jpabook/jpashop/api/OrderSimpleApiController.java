package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * xToOne(ManyToOne, OneToOne) Order Order -> Member Order -> Delivery
 */
/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // 이때 Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // ORDER 2
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<SimpleOrderDto> result =
                orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
        /*
         * 한 줄로 해도 됨 return orderRepository.findAllByCriteria(new OrderSearch()).stream()
         * .map(SimpleOrderDto::new) .collect(Collectors.toList());
         */
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        // ORDER 2
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result =
                orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
        /*
         * 한 줄로 해도 됨 return orderRepository.findAllWithMemberDelivery().stream()
         * .map(SimpleOrderDto::new) .collect(Collectors.toList());
         */
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        // ORDER 2
        return orderRepository.findOrderDtos();
        /*
         * 한 줄로 해도 됨 return orderRepository.findAllByCriteria(new OrderSearch()).stream()
         * .map(SimpleOrderDto::new) .collect(Collectors.toList());
         */
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY
        }
    }
}
