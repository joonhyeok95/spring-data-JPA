package jpabook.jpashop.repository;

import java.time.LocalDateTime;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate,
            OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; // LAZY
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; // LAZY
    }
}
