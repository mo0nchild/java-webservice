package application.domen.webapi.services.mappers.meeting;

import application.domen.webapi.models.commons.MeetingInfo;
import application.domen.webapi.models.commons.NotificationInfo;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.MeetingStatus;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MeetingMapper implements IMeetingMapper {
    @Override
    public MeetingInfo toInfo(MeetingEntity entity) {
        var meetingBuilder = MeetingInfo.builder()
                .ownerEmail(entity.getOwnerEmail())
                .meetingTime(entity.getMeetingTime())
                .uuid(entity.getUuid())
                .description(entity.getDescription())
                .name(entity.getName())
                .status(entity.getStatus())
                .place(entity.getPlace());
        return meetingBuilder.build();
    }
    @Override
    public MeetingEntity toEntity(NewMeetingInfo model) {
        var meetingBuilder = MeetingEntity.builder()
                .meetingTime(model.getMeetingTime())
                .description(model.getDescription())
                .place(model.getPlace())
                .name(model.getName())
                .status(MeetingStatus.NEWER)
                .ownerEmail(model.getOwnerEmail())
                .uuid(model.getUuid())
                .notifications(model.getMembers().stream()
                        .map(item -> NotificationEntity.builder()
                                .uuid(UUID.randomUUID())
                                .email(item)
                                .status(NotificationStatus.CHECKING)
                                .build()
                        ).collect(Collectors.toSet()));
        var mappedMeeting = meetingBuilder.build();
        mappedMeeting.getNotifications().forEach(item -> item.setMeeting(mappedMeeting));
        return mappedMeeting;
    }
    @Override
    public MeetingInfoResult toDto(MeetingEntity entity) {
        var meetingBuilder = MeetingInfoResult.builder()
                .meetingTime(entity.getMeetingTime())
                .description(entity.getDescription())
                .ownerEmail(entity.getOwnerEmail())
                .uuid(entity.getUuid())
                .place(entity.getPlace())
                .name(entity.getName())
                .status(entity.getStatus())
                .notifications(entity.getNotifications().stream()
                        .map(item -> NotificationInfo.builder()
                                .status(item.getStatus())
                                .uuid(item.getUuid())
                                .email(item.getEmail())
                                .build()
                        ).toList());
        return meetingBuilder.build();
    }
}
