package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Controller
public class HelloController {

    @GetMapping("hello")
    public String Hello(Model model) {
        model.addAttribute("data", "hello!!!");

        return "hello";
    }
}
