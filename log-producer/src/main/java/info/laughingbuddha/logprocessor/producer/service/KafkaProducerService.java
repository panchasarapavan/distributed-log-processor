package info.laughingbuddha.logprocessor.producer.service;

import info.laughingbuddha.dto.LogEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.kafka.topic.log-events}")
    private String logEventTopic;

    public void sendLogEvent(LogEventDto logEvent) {
        try {
            final String message = objectMapper.writeValueAsString(logEvent);

            final String partitionKey = logEvent.getOrganizationId();

            final CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
                    logEventTopic, partitionKey, message);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send log event: {}", logEvent.getId(), ex);
                    throw new RuntimeException("Failed to send log event", ex);
                } else {
                    log.debug("Sent log event: {} to partition: {}",
                            logEvent.getId(), result.getRecordMetadata().partition());
                }
            });
        } catch (Exception e) {
            log.error("Exception while processing logEvent: {}", logEvent.getId(), e);
            throw new RuntimeException("Failed to serialize log event", e);
        }
    }
}
