package application.domen.webapi.services.mappers.notification;

import application.domen.webapi.models.commons.NotificationInfo;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationMapper {
    public NotificationInfoResult toDto(NotificationEntity entity);
    public NotificationInfo toInfo(NotificationEntity entity);
}
