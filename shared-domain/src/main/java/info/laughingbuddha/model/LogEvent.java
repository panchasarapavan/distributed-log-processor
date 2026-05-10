package info.laughingbuddha.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import info.laughingbuddha.utils.AutoUlid;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_events")
@Data
@NoArgsConstructor
public class LogEvent {

    @Id
    @AutoUlid(monotonic = true)
    private String id;

    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "message", nullable = false, length = 4000)
    private String message;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "timestamp", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }
}
