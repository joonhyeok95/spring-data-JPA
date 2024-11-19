package jpabook.jpashop;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct // spring init 시점
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "종로", "1221");
            em.persist(member);

            Book book1 = createBook("JPA1 book", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 book", 10000, 100);
            em.persist(book2);

            OrderItem orderitem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderitem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderitem1, orderitem2);
            em.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("userB", "부산", "해운대", "3333");
            em.persist(member);

            Book book1 = createBook("Spring1 book", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("Spring2 book", 20000, 100);
            em.persist(book2);

            OrderItem orderitem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderitem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderitem1, orderitem2);
            em.persist(order);

        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stocQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stocQuantity);
            return book;
        }
    }
}

