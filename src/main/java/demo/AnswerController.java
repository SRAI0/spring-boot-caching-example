package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;

    @GetMapping("/e")
    public String index() throws InterruptedException {
        String model = ("answer"+ answerService.getAnswer());

        return model;
    }

    @GetMapping("/health")
    public String Health(){
        return "I'm Good.";
    }

}
