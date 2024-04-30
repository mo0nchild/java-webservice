package application.domen.webapi.services.notification;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.requests.UpdateMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.mappers.meeting.IMeetingMapper;
import application.domen.webapi.services.mappers.notification.INotificationMapper;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.publisher.IPublisherService;
import application.domen.webapi.services.repository.entities.MeetingStatus;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.repository.infrastructure.IUnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
@Setter
public class NotificationService implements INotificationService {
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private final IUnitOfWork dbContext;
    @Autowired
    private final INotificationMapper notificationMapper;
    @Autowired
    private final IMeetingMapper meetingMapper;
    @Autowired
    private final IPublisherService publisher;
    @Override
    public void addNotification(NewMeetingInfo info) throws NotificationError {
        try {
            var mappedInfo = this.meetingMapper.toEntity(info);
            var meeting = this.dbContext.getMeetingRepository().getByUUID(mappedInfo.getUuid());
            if (meeting.isPresent()) {
                throw new Exception("Запись мероприятия уже существует");
            }
            for (var item : mappedInfo.getNotifications()) {
                if (!this.publisher.publishInviteMessage(this.notificationMapper.toDto(item),
                        "Приглашаем вас посетить мероприятие!")) {
                    throw new Exception("Не удалось отправить сообщение");
                }
            }
            var result = this.dbContext.getMeetingRepository().save(mappedInfo);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError(String.format("Не удалось добавить запись: %s", error.getMessage()));
        }
    }
    @Override
    public void updateNotification(UpdateMeetingInfo info) throws NotificationError {
        try {
            var record = this.dbContext.getMeetingRepository().getByUUID(info.getUuid())
                    .orElseThrow(() -> new Exception("Запись не найдена"));
            record.setDescription(info.getDescription());
            record.setName(info.getName());

            record.setMeetingTime(info.getMeetingTime());
            record.setPlace(info.getPlace());
            record.setStatus(MeetingStatus.UPDATED);
            record.getNotifications().forEach(item -> {
                if (item.getStatus() != NotificationStatus.ACCEPTED) return;
                item.setStatus(NotificationStatus.CHECKING);
            });
            for(var notification : record.getNotifications()) {
                var message = this.notificationMapper.toDto(notification);
                this.publisher.publishInviteMessage(message, "Условия мероприятия изменились");
            }
            this.dbContext.getMeetingRepository().save(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError(String.format("Не удалось изменить запись: %s", error.getMessage()));
        }
    }
    @Override
    public void removeNotification(UUID uuid) throws NotificationError {
        try {
            var record = this.dbContext.getNotificationRepository().getByUuid(uuid)
                    .orElseThrow(() -> new Exception("Не удалось найти запись"));
            this.dbContext.getNotificationRepository().delete(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError(String.format("Не удалось удалить запись: %s", error.getMessage()));
        }
    }
    @Override
    public void removeMeeting(UUID uuid) throws NotificationError {
        try {
            var record = this.dbContext.getMeetingRepository().getByUUID(uuid)
                    .orElseThrow(() -> new Exception("Не удалось найти запись"));
            this.dbContext.getMeetingRepository().delete(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError(String.format("Не удалось удалить запись: %s", error.getMessage()));
        }
    }
    @Override
    public void cancelMeeting(UUID uuid) throws NotificationError {
        try {
            var record = this.dbContext.getMeetingRepository().getByUUID(uuid)
                    .orElseThrow(() -> new Exception("Запись не найдена"));
            for(var notification : record.getNotifications()) {
                if (notification.getStatus() == NotificationStatus.REJECTED) continue;
                var message = this.notificationMapper.toDto(notification);
                this.publisher.publishUpdateMessage(message, "Мероприятие было отменено");
            }
            record.setStatus(MeetingStatus.CANCELED);
            record.getNotifications().forEach(item -> {
                if (item.getStatus() == NotificationStatus.REJECTED) return;
                item.setStatus(NotificationStatus.CHECKING);
            });
            this.dbContext.getMeetingRepository().save(record);
        }
        catch (Exception error) {
            this.logger.info(error.getMessage());
            throw new NotificationError(String.format("Не удалось отменить: %s", error.getMessage()));
        }
    }
    @Override
    public void changeStatus(ChangeStatusInfo info) throws NotificationError {
        try {
            var record = this.dbContext.getNotificationRepository().getByUuid(info.getUuid())
                    .orElseThrow(() -> new Exception("Не удалось найти запись"));
            if (record.getMeeting().getStatus() == MeetingStatus.CANCELED && info.getStatus() != NotificationStatus.CHECKED) {
                throw new NotificationError("Мероприятие было отменено");
            }
            record.setStatus(info.getStatus());
            this.dbContext.getNotificationRepository().save(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError(String.format("Не удалось изменить статус: %s", error.getMessage()));
        }
    }
    @Override
    public Optional<NotificationInfoResult> getByUuid(UUID id) {
        return this.dbContext.getNotificationRepository()
                .getByUuid(id).map(this.notificationMapper::toDto).or(Optional::empty);
    }
    @Override
    public List<NotificationInfoResult> getAllByEmail(String email) {
        return this.dbContext.getNotificationRepository()
                .getByEmail(email).stream().map(this.notificationMapper::toDto).toList();
    }
    @Override
    public List<NotificationInfoResult> getAllByEmail(String email, NotificationStatus status) {
        return this.dbContext.getNotificationRepository()
                .getByEmail(email, status).stream().map(this.notificationMapper::toDto).toList();
    }
    @Override
    public Optional<MeetingInfoResult> getByMeeting(UUID uuid) {
        return this.dbContext.getMeetingRepository()
                .getByUUID(uuid).map(this.meetingMapper::toDto).or(Optional::empty);
    }
    @Override
    public Optional<MeetingInfoResult> getByMeeting(UUID uuid, NotificationStatus status) {
        var meetingRecord = this.dbContext.getMeetingRepository()
                .getByUUID(uuid).map(this.meetingMapper::toDto).or(Optional::empty);
        meetingRecord.ifPresent(meeting -> {
            var notifications = meeting.getNotifications().stream()
                    .filter(item -> item.getStatus() == status).toList();
            meeting.setNotifications(notifications);
        });
        return meetingRecord;
    }
    @Override
    public List<NotificationInfoResult> getAll() {
        return this.dbContext.getNotificationRepository()
                .getAll().stream().map(this.notificationMapper::toDto).toList();
    }
    @Override
    public List<MeetingInfoResult> getByOwnerEmail(String email) {
        return this.dbContext.getMeetingRepository().getByOwnerEmail(email)
                .stream().map(this.meetingMapper::toDto).toList();
    }
    @Override
    public List<MeetingInfoResult> getAllMeetings() {
        return this.dbContext.getMeetingRepository()
                .getAll().stream().map(this.meetingMapper::toDto).toList();
    }
}
