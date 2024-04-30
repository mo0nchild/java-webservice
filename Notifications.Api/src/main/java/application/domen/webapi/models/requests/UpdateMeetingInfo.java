package application.domen.webapi.models.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class UpdateMeetingInfo {
    private String name;
    private String description;
    private UUID uuid;
    private String place;
    private LocalDateTime meetingTime;
}
