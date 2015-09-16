package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MathController {
    @Autowired
    AnswerService answerService;

    @RequestMapping("/")
    public String index(Model model) throws InterruptedException {
        model.addAttribute("answer", answerService.getAnswerWithComplicatedMath());

        return "index";
    }
}
