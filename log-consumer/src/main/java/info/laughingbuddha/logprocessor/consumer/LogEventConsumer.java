package info.laughingbuddha.logprocessor.consumer;

import info.laughingbuddha.logprocessor.model.LogEvent;
import info.laughingbuddha.logprocessor.repository.LogEventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.KafkaHeaders;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LogEventConsumer {

    @Autowired
    private LogEventRepository logEventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Counter processedLogEventCounter;
    private final Counter errorLogEventCounter;

    public LogEventConsumer(MeterRegistry meterRegistry) {
        this.processedLogEventCounter = Counter.builder("log_events_processed_total")
                .description("Total number of log events processed")
                .register(meterRegistry);
        this.errorLogEventCounter = Counter.builder("log_events_error_total")
                .description("Total number of log events with errors")
                .register(meterRegistry);
    }


    @KafkaListener(topics = "log-events", groupId = "log-consumer-group")
    public void consume(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partitionId,
            Acknowledgment acknowledgment) {
        try {
            log.debug("Recieved message: {} from topic: {} partition: {}", message, topic, partitionId);

            LogEvent logEvent = objectMapper.readValue(message, LogEvent.class);
            processLogEvent(logEvent);

            acknowledgment.acknowledge();
            processedLogEventCounter.increment();

            log.debug("Successfully processed message: {} from topic: {} partition: {}", message, topic, partitionId);
        } catch (Exception e) {
            log.error("Failed to process message: {}", message, e);
            errorLogEventCounter.increment();
            throw new RuntimeException("Failed to process log event", e);
        }
    }

    @Retryable(value = {DataAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processLogEvent(LogEvent logEvent) {
        try {
            // Set processing timestamp
            logEvent.setProcessedAt(LocalDateTime.now());

            // Save to database
            logEventRepository.save(logEvent);

            // Additional processing based on log level
            if ("ERROR".equals(logEvent.getLevel())) {
                handleErrorLog(logEvent);
            }

            log.info("Processed log event: {} for organization: {}",
                    logEvent.getId(), logEvent.getOrganizationId());

        } catch (DataAccessException e) {
            log.error("Database error processing log event: {}", logEvent.getId(), e);
            throw e;
        }
    }

    private void handleErrorLog(LogEvent logEvent) {
        // Additional error log processing (e.g., alerting, metrics)
        log.warn("Error log detected: {} from {}", logEvent.getMessage(), logEvent.getSource());

        // Could trigger alerts, update error counters, etc.
        // For now, just log the error
    }
}
