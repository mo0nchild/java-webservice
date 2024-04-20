package application.domen.webapi.services.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private boolean status;

    @Column(name = "auditory_id")
    private int auditoryId;

    @Column(name = "message")
    private String message;

    @Column(name = "meeting_time")
    private LocalDateTime meetingTime;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "meeting_id")
    private UUID meetingId;
}
