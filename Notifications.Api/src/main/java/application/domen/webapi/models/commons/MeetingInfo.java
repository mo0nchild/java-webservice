package application.domen.webapi.models.commons;

import application.domen.webapi.services.repository.entities.MeetingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class MeetingInfo {
    private String name;
    private String description;
    private UUID uuid;
    private MeetingStatus status;

    private String place;
    private LocalDateTime meetingTime;
    private String ownerEmail;
}
