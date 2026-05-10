package info.laughingbuddha.logprocessor.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "info.laughingbuddha.logprocessor.producer",  // Current package
        "info.laughingbuddha"  // Include shared-domain components
})
public class LogProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogProducerApplication.class, args);
    }
}
