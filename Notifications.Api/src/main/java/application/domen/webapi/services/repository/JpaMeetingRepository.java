package application.domen.webapi.services.repository;

import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.repository.infrastructure.MeetingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaMeetingRepository extends JpaRepository<MeetingEntity, Long>, MeetingRepository {
    @Query("SELECT u FROM meetings u WHERE u.uuid = ?1")
    @Override
    public Optional<MeetingEntity> getByUUID(UUID meetingId);
    @Query("SELECT u FROM meetings u WHERE u.ownerEmail = ?1")
    @Override
    public List<MeetingEntity> getByOwnerEmail(String email);

    @Query("SELECT u FROM meetings u")
    @Override
    public List<MeetingEntity> getAll();
}
