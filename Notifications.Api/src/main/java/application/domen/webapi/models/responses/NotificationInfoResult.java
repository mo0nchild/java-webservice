package application.domen.webapi.models.responses;

import application.domen.webapi.models.commons.MeetingInfo;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationInfoResult implements IResponseInfo {
    private String email;
    private NotificationStatus status;
    private UUID uuid;
    private MeetingInfo meeting;
}
