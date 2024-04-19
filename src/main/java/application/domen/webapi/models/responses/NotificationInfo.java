package application.domen.webapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationInfo {
    private String email;
    private LocalDateTime meetingTime;
    private Integer auditoryId;
}
