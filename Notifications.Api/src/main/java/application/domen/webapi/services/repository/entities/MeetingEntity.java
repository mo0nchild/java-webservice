package application.domen.webapi.services.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity(name = "meetings")
public class MeetingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "place")
    private String place;

    @Column(name = "time")
    private LocalDateTime meetingTime;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private MeetingStatus status;

    @Column(name = "owner_email")
    private String ownerEmail;

    @OneToMany(mappedBy="meeting", cascade = {CascadeType.ALL})
    private Set<NotificationEntity> notifications;
}
