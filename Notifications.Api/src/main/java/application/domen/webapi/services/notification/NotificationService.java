package application.domen.webapi.services.notification;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.mapper.NotificationMapper;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.publisher.IPublisherService;
import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.services.repository.NotificationRepository;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private final NotificationRepository repository;
    @Autowired
    private final NotificationMapper mapper;
    @Autowired
    private final IPublisherService publisher;
    @Autowired
    private final Environment environment;
    @Override
    public void addNotification(NewNotificationInfo info) throws NotificationError {
        try {
            var mappedInfo = this.mapper.toEntity(info);
            if(!this.publisher.publishMessage(info, mappedInfo.getUuid())) {
                throw new Exception("Не удалось отправить сообщение");
            }
            var result = this.repository.save(mappedInfo);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError("Не удалось добавить запись");
        }
    }
    @Override
    public void removeNotification(UUID uuid) throws NotificationError {
        try {
            var record = this.repository.getByUuid(uuid).orElseThrow(Exception::new);
            this.repository.delete(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError("Не удалось добавить запись");
        }
    }
    @Override
    public void changeStatus(ChangeStatusInfo info) throws NotificationError {
        try {
            var record = this.repository.getByUuid(info.getUuid()).orElseThrow(Exception::new);
            record.setStatus(info.getStatus());
            this.repository.save(record);
        }
        catch (Exception error) {
            this.logger.warn(error.getMessage());
            throw new NotificationError("Не удалось изменить статус");
        }
    }
    @Override
    public Optional<NotificationInfo> getByUuid(UUID id) {
        return this.repository.getByUuid(id).map(this.mapper::toDto).or(Optional::empty);
    }
    @Override
    public List<NotificationInfo> getAllByEmail(String email) {
        return this.repository.getByEmail(email).stream().map(this.mapper::toDto).toList();
    }
    @Override
    public List<NotificationInfo> getAllByEmail(String email, NotificationStatus status) {
        return this.repository.getByEmail(email, status).stream().map(this.mapper::toDto).toList();
    }
    @Override
    public List<NotificationInfo> getByMeeting(UUID uuid) {
        return this.repository.getByMeeting(uuid).stream().map(this.mapper::toDto).toList();
    }
    @Override
    public List<NotificationInfo> getByMeeting(UUID uuid, NotificationStatus status) {
        return this.repository.getByMeeting(uuid, status).stream().map(this.mapper::toDto).toList();
    }
    @Override
    public List<NotificationInfo> getAll() {
        return this.repository.getAll().stream().map(this.mapper::toDto).toList();
    }
}
