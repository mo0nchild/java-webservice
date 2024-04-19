package application.domen.webapi.models.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewNotificationInfo {
    private String email;
    private LocalDateTime meetingTime;
    private Integer auditoryId;
}
