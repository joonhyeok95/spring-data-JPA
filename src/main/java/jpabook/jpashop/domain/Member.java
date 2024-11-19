package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded // 내장타입이라는 선언
    private Address address;

    @JsonIgnore // "oders": [] 값을 빼기위한 임시조치 테스트
    @OneToMany(mappedBy = "member") // order 테이블의 member 필드로 인해 맵핑된거야, 난 거울일 뿐!
    private List<Order> oders = new ArrayList<>();
}
