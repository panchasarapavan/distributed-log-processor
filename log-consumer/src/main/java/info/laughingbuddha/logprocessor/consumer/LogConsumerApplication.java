package info.laughingbuddha.logprocessor.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EntityScan(basePackages = "info.laughingbuddha.model")
@EnableJpaRepositories(basePackages = "info.laughingbuddha.repository")
public class LogConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogConsumerApplication.class, args);
    }
}
