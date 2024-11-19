package jpabook.jpashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * ... Description ...
 * 
 * @author joonhyeok.lim
 * @email dkttkemf@gmail.com
 * @since 2024. 11. 19.
 * @version
 */
@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
    }
}
