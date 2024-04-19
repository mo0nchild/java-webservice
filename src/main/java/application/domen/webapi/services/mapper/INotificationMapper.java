package application.domen.webapi.services.mapper;

import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.mapstruct.*;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationMapper {
    public NotificationInfo toDto(NotificationEntity entity);
    public NotificationEntity toEntity(NotificationInfo model);
}
