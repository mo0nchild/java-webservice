package application.domen.webapi.services.repository;

import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaMeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query("SELECT u FROM meetings u WHERE u.uuid = ?1")
    public Optional<MeetingEntity> getByUUID(UUID meetingId);
    @Query("SELECT u FROM meetings u WHERE u.ownerEmail = ?1")
    public List<MeetingEntity> getByOwnerEmail(String email);
    @Query("SELECT u FROM meetings u")
    public List<MeetingEntity> getAll();
}
