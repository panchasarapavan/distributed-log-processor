package info.laughingbuddha.logprocessor.producer.service;

import info.laughingbuddha.logprocessor.producer.model.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.kafka.topic.log-events}")
    private String logEventTopic;

    public void sendLogEvent(LogEvent logEvent) {
        try {
            final String message = objectMapper.writeValueAsString(logEvent);

            final String partitionKey = logEvent.getOrganizationId();

            final CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
                    logEventTopic, partitionKey, message);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send log event: {}", logEvent.getId(), ex);
                    throw new RuntimeException("Failed to send log event", ex);
                } else {
                    logger.debug("Sent log event: {} to partition: {}",
                            logEvent.getId(), result.getRecordMetadata().partition());
                }
            });
        } catch (Exception e) {
            logger.error("Exception while processing logEvent: {}", logEvent.getId(), e);
            throw new RuntimeException("Failed to serialize log event", e);
        }
    }
}
