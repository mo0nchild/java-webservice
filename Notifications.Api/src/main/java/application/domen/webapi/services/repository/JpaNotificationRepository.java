package application.domen.webapi.services.repository;

import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.repository.infrastructure.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationRepository extends NotificationRepository,
        JpaRepository<NotificationEntity, Long> {
    @Query("SELECT u FROM notifications u WHERE u.id = ?1")
    @Override
    public Optional<NotificationEntity> getById(Integer id);
    @Query("SELECT u FROM notifications u WHERE u.uuid = ?1")
    @Override
    public Optional<NotificationEntity> getByUuid(UUID id);
    @Query("SELECT u FROM notifications u")
    @Override
    public List<NotificationEntity> getAll();

    @Query("SELECT u FROM notifications u WHERE u.email = ?1")
    @Override
    public List<NotificationEntity> getByEmail(String email);
    @Query("SELECT u FROM notifications u WHERE u.email = ?1 AND u.status = ?2")
    @Override
    public List<NotificationEntity> getByEmail(String email, NotificationStatus status);
}
