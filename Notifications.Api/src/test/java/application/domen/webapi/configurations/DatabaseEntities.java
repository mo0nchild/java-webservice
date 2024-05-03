package application.domen.webapi.configurations;

import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.MeetingStatus;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseEntities {
    private static List<MeetingEntity> databaseMock = new ArrayList<>();
    public static List<NotificationEntity> getNotifications() {
        var notifications = new ArrayList<NotificationEntity>();
        for (var meeting : databaseMock) {
            notifications.addAll(meeting.getNotifications());
        }
        return notifications;
    }
    public static List<MeetingEntity> getMeetings() { return databaseMock; }
    public static void generateEntities() {
        databaseMock.clear();
        databaseMock.addAll(new ArrayList<>(){{
            add(MeetingEntity.builder().id(0L)
                    .uuid(UUID.fromString("63bf3e79-7a16-40ba-b0ee-d74193db081a"))
                    .place("Аудитория 10")
                    .name("Уборка территории")
                    .status(MeetingStatus.NEWER)
                    .description("Приглашаем вас на мероприятие")
                    .ownerEmail("owner1@gmail.com")
                    .meetingTime(LocalDateTime.now())
                    .notifications(Arrays.stream(new NotificationEntity[] {
                            NotificationEntity.builder().id(0L)
                                    .email("user0@gmail.com")
                                    .uuid(UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366"))
                                    .status(NotificationStatus.CHECKING)
                                    .build(),
                            NotificationEntity.builder().id(1L)
                                    .email("user1@gmail.com")
                                    .uuid(UUID.fromString("3ea7ecbe-c715-4f80-a65e-7e75229bfeac"))
                                    .status(NotificationStatus.CHECKING)
                                    .build(),
                            NotificationEntity.builder().id(2L)
                                    .email("user2@gmail.com")
                                    .uuid(UUID.fromString("1f39d1f1-195e-485e-843d-2978841d185a"))
                                    .status(NotificationStatus.ACCEPTED)
                                    .build(),
                    }).collect(Collectors.toSet()))
                    .build()
            );
            add(MeetingEntity.builder().id(1L)
                    .uuid(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"))
                    .place("Аудитория 20")
                    .name("Просмотр фильма")
                    .status(MeetingStatus.UPDATED)
                    .description("Приглашаем вас на мероприятие")
                    .ownerEmail("owner2@gmail.com")
                    .meetingTime(LocalDateTime.now())
                    .notifications(Arrays.stream(new NotificationEntity[] {
                            NotificationEntity.builder().id(3L)
                                    .email("user3@gmail.com")
                                    .uuid(UUID.fromString("a99847f8-8aa7-460e-80aa-ac18b6676db6"))
                                    .status(NotificationStatus.REJECTED)
                                    .build(),
                            NotificationEntity.builder().id(4L)
                                    .email("user2@gmail.com")
                                    .uuid(UUID.fromString("acb2bbb9-ab6d-476b-8b81-66b512b73805"))
                                    .status(NotificationStatus.CHECKING)
                                    .build(),
                            NotificationEntity.builder().id(5L)
                                    .email("user5@gmail.com")
                                    .uuid(UUID.fromString("43c2411e-2c0d-405a-b6e7-6a93ce33ac2d"))
                                    .status(NotificationStatus.ACCEPTED)
                                    .build(),
                    }).collect(Collectors.toSet()))
                    .build()
            );

        }});
        for (var meeting : databaseMock) {
            meeting.getNotifications().forEach(item -> item.setMeeting(meeting));
        }
    }
}
