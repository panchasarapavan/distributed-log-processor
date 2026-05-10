package info.laughingbuddha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LogEventDto {
    private String id;
    private String organizationId;
    private String level;
    private String message;
    private String source;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public LogEventDto() {
        this.timestamp = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
    }
}
