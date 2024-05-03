package application.domen.webapi.models.requests;

import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class ChangeStatusInfo {
    private UUID uuid;
    private NotificationStatus status;
}
