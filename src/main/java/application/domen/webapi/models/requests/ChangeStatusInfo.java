package application.domen.webapi.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class ChangeStatusInfo {
    private UUID uuid;
    private boolean status;
}
