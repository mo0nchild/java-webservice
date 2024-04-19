package application.domen.webapi.services.mapper;

import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationMapper {
    public NotificationInfo toDto(NotificationEntity entity);
    public NotificationEntity toEntity(NewNotificationInfo model);
}
