package info.laughingbuddha.logprocessor.repository;

import info.laughingbuddha.logprocessor.model.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEventRepository extends JpaRepository<LogEvent, String> {

    List<LogEvent> findByOrganizationIdAndTimestampBetween(
            String organizationId,
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT COUNT(e) FROM LogEvent e WHERE e.organizationId = :orgId AND e.level = :level")
    long countByOrganizationIdAndLevel(
            @Param("orgId") String organizationId,
            @Param("level") String level
    );

    List<LogEvent> findByLevelAndTimestampAfter(String level, LocalDateTime timestamp);
}
