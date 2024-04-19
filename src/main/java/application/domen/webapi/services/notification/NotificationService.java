package application.domen.webapi.services.notification;

import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.mapper.NotificationMapper;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Setter
public class NotificationService implements INotificationService {
    @Autowired
    private final NotificationRepository repository;
    @Autowired
    private final NotificationMapper mapper;
    @Autowired
    private final JavaMailSender emailSender;
    @Autowired
    private final Environment enviroment;
    @Override
    public void addNotification(NewNotificationInfo info) throws NotificationError {
        try {
            var senderEmail = this.enviroment.getProperty("spring.mail.username");
            this.emailSender.send(new SimpleMailMessage() {{
                setFrom(senderEmail);
                setTo(info.getEmail());
                setSubject("Приглашение на встречу");
                setText(String.format("Время встречи: %s", info.getMeetingTime()));
            }});
            this.repository.save(this.mapper.toEntity(info));
        }
        catch (Exception error) {
            System.out.println(error.getMessage());
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
            throw new NotificationError("Не удалось добавить запись");
        }
    }
    @Override
    public Optional<NotificationInfo> getByUuid(UUID id) {
        var record = this.repository.getByUuid(id);
        return record.map(this.mapper::toDto).or(Optional::empty);
    }
    @Override
    public List<NotificationInfo> getAllByEmail(String email) {
        return this.repository.getByEmail(email).stream().map(this.mapper::toDto).toList();
    }
    @Override
    public List<NotificationInfo> getAll() {
        return this.repository.getAll().stream().map(this.mapper::toDto).toList();
    }
}
