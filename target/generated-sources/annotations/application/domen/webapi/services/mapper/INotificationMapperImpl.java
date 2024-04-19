package application.domen.webapi.services.mapper;

import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-19T13:48:42+0300",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class INotificationMapperImpl implements INotificationMapper {

    @Override
    public NotificationInfo toDto(NotificationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationInfo notificationInfo = new NotificationInfo();

        return notificationInfo;
    }

    @Override
    public NotificationEntity toEntity(NotificationInfo model) {
        if ( model == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        return notificationEntity;
    }
}
