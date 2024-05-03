package application.domen.webapi.services.repository.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationStatus {
    CHECKING("checking"), CHECKED("checked"),
    REJECTED("rejected"), ACCEPTED("accepted");
    private final String name;
}
