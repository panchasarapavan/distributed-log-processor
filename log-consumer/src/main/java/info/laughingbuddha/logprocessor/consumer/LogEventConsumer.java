package info.laughingbuddha.logprocessor.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogEventConsumer {

    @Autowired
    private LogEventRepository logEventRepository;

    public void consumeLogEvent(String message) {
        try {
            LogEvent event = objectMapper.readValue(message, LogEvent.class);
            event.setProcessedAt(LocalDateTime.now());
            logEventRepository.save(event);

            logger.info("Processed log event: {}", event.getId());
        } catch (Exception e) {
            logger.error("Failed to process message: {}", message, e);
        }
    }

}
