package application.domen.webapi.services.repository.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MeetingStatus {
    NEWER("newer"), UPDATED("updated"), CANCELED("canceled");
    private final String name;
}
