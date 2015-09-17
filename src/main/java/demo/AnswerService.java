package demo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    @Cacheable("answer")
    public int getAnswer() throws InterruptedException {
        Thread.sleep(5000);
        return 42;
    }
}
