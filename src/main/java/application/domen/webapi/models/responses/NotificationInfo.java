package application.domen.webapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationInfo {
    private UUID notificationId;
    private String email;
    private LocalDateTime meetingTime;
    private Integer auditoryId;
}
