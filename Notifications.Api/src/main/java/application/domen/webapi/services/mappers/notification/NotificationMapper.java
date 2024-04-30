package application.domen.webapi.services.mappers.notification;

import application.domen.webapi.models.commons.MeetingInfo;
import application.domen.webapi.models.commons.NotificationInfo;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class NotificationMapper implements INotificationMapper {
    @Override
    public NotificationInfoResult toDto(NotificationEntity entity) {
        var notificationBuilder = NotificationInfoResult.builder()
                .email(entity.getEmail())
                .uuid(entity.getUuid())
                .status(entity.getStatus())
                .meeting(MeetingInfo.builder()
                        .ownerEmail(entity.getMeeting().getOwnerEmail())
                        .place(entity.getMeeting().getPlace())
                        .description(entity.getMeeting().getDescription())
                        .name(entity.getMeeting().getName())
                        .status(entity.getMeeting().getStatus())
                        .uuid(entity.getMeeting().getUuid())
                        .meetingTime(entity.getMeeting().getMeetingTime())
                        .build());
        return notificationBuilder.build();
    }
    @Override
    public NotificationInfo toInfo(NotificationEntity entity) {
        var notificationBuilder = NotificationInfo.builder()
                .email(entity.getEmail())
                .uuid(entity.getUuid())
                .status(entity.getStatus());
        return notificationBuilder.build();
    }
}
