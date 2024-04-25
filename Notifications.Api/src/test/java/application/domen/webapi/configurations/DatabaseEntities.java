package application.domen.webapi.configurations;

import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseEntities {
    public static List<NotificationEntity> generateEntities() {
        return new ArrayList<NotificationEntity>() {{
            add(NotificationEntity.builder()
                    .id(0L)
                    .meetingId(UUID.fromString("12ff3af1-d103-433c-a62e-a51560462d63"))
                    .email("user1@gmail.com")
                    .status(NotificationStatus.CHECKING)
                    .message("Приглащение на встречу")
                    .meetingTime(LocalDateTime.now())
                    .auditoryId(0)
                    .uuid(UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11"))
                    .build()
            );
            add(NotificationEntity.builder()
                    .id(1L)
                    .meetingId(UUID.fromString("ea67e6b5-7a53-4400-9cb6-4cdf56bbeeb4"))
                    .email("user1@gmail.com")
                    .status(NotificationStatus.ACCEPTED)
                    .message("Приглащение на встречу")
                    .meetingTime(LocalDateTime.now())
                    .auditoryId(1)
                    .uuid(UUID.fromString("f73c5145-e37e-4f34-a460-b6f6a26b0d10"))
                    .build()
            );
            add(NotificationEntity.builder()
                    .id(2L)
                    .meetingId(UUID.fromString("12ff3af1-d103-433c-a62e-a51560462d63"))
                    .email("user2@gmail.com")
                    .status(NotificationStatus.REJECTED)
                    .message("Приглащение на встречу")
                    .meetingTime(LocalDateTime.now())
                    .auditoryId(2)
                    .uuid(UUID.fromString("d3197142-b4dc-4cb8-931f-ee7281107f8f"))
                    .build()
            );
        }};
    }
}
