package application.domen.webapi.services.repository.infrastructure;

import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    public Optional<NotificationEntity> getById(Integer id);
    public Optional<NotificationEntity> getByUuid(UUID id);
    public List<NotificationEntity> getAll();

    public List<NotificationEntity> getByEmail(String email, NotificationStatus status);
    public List<NotificationEntity> getByEmail(String email);
    public List<NotificationEntity> getByMeeting(UUID meetingId, NotificationStatus status);
    public List<NotificationEntity> getByMeeting(UUID meetingId);

    <T extends NotificationEntity> T save(T entity);
    void delete(NotificationEntity entity);
}
