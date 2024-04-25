package application.domen.webapi.models.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NewNotificationInfo {
    private String email;
    private LocalDateTime meetingTime;
    private String message;

    private Integer auditoryId;
    private UUID meetingId;
}
