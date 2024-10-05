package demo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    //@Cacheable("answer")
    public int getAnswer() throws InterruptedException {
        Thread.sleep(500);
        return 1234567890;
    }
}
