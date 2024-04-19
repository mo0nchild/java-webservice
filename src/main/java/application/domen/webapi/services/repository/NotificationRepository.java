package application.domen.webapi.services.repository;

import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    @Query("SELECT u FROM notifications u")
    public List<NotificationEntity> getAll();

    @Query("SELECT u FROM notifications u WHERE u.id = ?1")
    public NotificationEntity getById(Integer id);

    @Query("SELECT u FROM notifications u WHERE u.email = ?1")
    public NotificationEntity getByEmail(String email);
}
