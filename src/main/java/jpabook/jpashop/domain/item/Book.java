package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;

}
