package jpabook.jpashop;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;

@SpringBootTest
@Transactional
public class OrderServiceTest {

	@Autowired EntityManager em;
	@Autowired OrderService orderService;
	@Autowired OrderRepository orderRepository;
	
	@Test
	public void 상품주문() throws Exception{
		//given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("인천","남동구","111-111"));
		em.persist(member);
		
		Book book = new Book();
		book.setName("JJJBOO");
		book.setPrice(9090);
		book.setStockQuantity(10);
		em.persist(book);
		
		int orderCount = 2;
		
		//when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		//then
		Order getOrder = orderRepository.findOne(orderId);
		

		assertEquals(OrderStatus.ORDER, getOrder.getStatus()
				, "상품 주문시 상태는  ORDER");
		assertEquals(1, getOrder.getOrderItems().size()
				, "주문한 상품 종류 수가 정확해야 한다.");
		assertEquals(9090 * orderCount, getOrder.getTotalPrice()
				, "주문 가격은 가격 * 수량이다.");
		assertEquals(8, book.getStockQuantity()
				, "주문 수량만큼 재고가 줄어야 한다.");
		
	}

	@Test
	public void 상품취소() throws Exception{
		//given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("인천","남동구","111-111"));
		em.persist(member);
		
		Item item = new Book();
		item.setName("JJJBOO");
		item.setPrice(9090);
		item.setStockQuantity(10);
		em.persist(item);
		
		int orderCount = 1;
		
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		//when
		orderService.cancelOrder(orderId);
		
		//then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals(10, item.getStockQuantity());
	}

//	@Test(expected = NotEnoughStockException.class) // Junit4
	@Test
	public void 상품주문_재고수량초과() throws Exception {
		//given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("인천","남동구","111-111"));
		em.persist(member);
		
		Item item = new Book();
		item.setName("JJJBOO");
		item.setPrice(9090);
		item.setStockQuantity(10);
		em.persist(item);
		
		int orderCount = 11;
		//when
		assertThrows(NotEnoughStockException.class, () -> {
			orderService.order(member.getId(), item.getId(), orderCount);
		});
		
		//then
//		fail("재고 수량 부족 예외가가 발생해야 한다."); // Junit4
		
	}
	
}
