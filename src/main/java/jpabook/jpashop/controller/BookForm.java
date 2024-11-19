package jpabook.jpashop.controller;

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
@Getter
@Setter
public class BookForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
