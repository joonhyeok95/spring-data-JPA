package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 테이블을 나눌지, 하나로할지 전략을 구성..
@DiscriminatorColumn(name = "dtype") // 하나로하기 때문에 구분할 컬럼
public abstract class Item {

	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;
	
	private String name;
	private int price;
	private int stockQuantity;
	
	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();
	
	//비즈니스 로직-상품 엔티티 개발
	/*
	 * 재고 증가
	 */
	public void addStack(int quantity) {
		this.stockQuantity += quantity;
	}
	/*
	 * 재고 감소
	 */
	public void removeStock(int quantity) {
		int realStock = this.stockQuantity - quantity;
		if(realStock < 0) {
			throw new NotEnoughStockException("재고가 0보다 작을 수 없습니다.");
		}
		this.stockQuantity = realStock;
	}
}
