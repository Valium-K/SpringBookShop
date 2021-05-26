package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")    // hello 라는 url에 오면 해당 컨트롤러를 호출한다.
    public String hello(Model model) {

        // Controller에서 view에 값을 model에 담아서 넘길 수 있다.
        model.addAttribute("data", "hello!!");

        return "hello"; // "hello"에 자동으로 .html이 붙는다. 확장자는 빼는게 관례.
    }
}

