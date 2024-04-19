package application.domen.webapi.services.mapper;

import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationMapper implements INotificationMapper {
    @Override
    public NotificationInfo toDto(NotificationEntity entity) {
        var builder = NotificationInfo.builder()
                .email(entity.getEmail())
                .meetingTime(entity.getMeetingTime())
                .auditoryId(entity.getAuditoryId());
        return builder.build();
    }
    @Override
    public NotificationEntity toEntity(NotificationInfo model) {
        var builder = NotificationEntity.builder()
                .email(model.getEmail())
                .meetingTime(model.getMeetingTime())
                .auditoryId(model.getAuditoryId())
                .id("");
        return builder.build();
    }
}
