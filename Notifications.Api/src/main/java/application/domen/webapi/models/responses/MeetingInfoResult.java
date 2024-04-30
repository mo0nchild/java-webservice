package application.domen.webapi.models.responses;

import application.domen.webapi.models.commons.NotificationInfo;
import application.domen.webapi.services.repository.entities.MeetingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class MeetingInfoResult implements IResponseInfo {
    private String name;
    private String description;
    private UUID uuid;
    private MeetingStatus status;

    private String place;
    private LocalDateTime meetingTime;
    private String ownerEmail;
    private List<NotificationInfo> notifications;
}
