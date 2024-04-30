package application.domen.webapi.models.commons;

import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class NotificationInfo {
    private String email;
    private NotificationStatus status;
    private UUID uuid;
}
