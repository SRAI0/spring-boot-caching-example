package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AnswerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnswerApplication.class, args);
    }
}
