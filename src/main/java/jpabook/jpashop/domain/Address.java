package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
	private String city;
	private String street;
	private String zipcode;
	
	// JPA 스펙상 만들어 놓은 것 -> 함부로 생성하면 안되겠네~
	protected Address() {
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
	
}
