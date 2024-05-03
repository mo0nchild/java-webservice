package application.domen.webapi.services.repository.infrastructure;

import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository {
    public Optional<MeetingEntity> getByUUID(UUID meetingId);
    public List<MeetingEntity> getByOwnerEmail(String email);
    public List<MeetingEntity> getAll();

    <T extends MeetingEntity> T save(T entity);
    void delete(MeetingEntity entity);
}
