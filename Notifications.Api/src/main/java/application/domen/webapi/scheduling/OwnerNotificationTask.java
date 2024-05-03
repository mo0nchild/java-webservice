package application.domen.webapi.scheduling;

import application.domen.webapi.services.mappers.meeting.IMeetingMapper;
import application.domen.webapi.services.publisher.IPublisherService;
import application.domen.webapi.services.repository.infrastructure.IUnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.text.StyledEditorKit;

@Component
@AllArgsConstructor
@Getter
public class OwnerNotificationTask {
    private final Logger logger = LoggerFactory.getLogger(OwnerNotificationTask.class);
    @Autowired
    private final IUnitOfWork dbContext;
    @Autowired
    private final IPublisherService publisherService;
    @Autowired
    private final IMeetingMapper meetingMapper;
    @Autowired
    private final Environment environment;
    @Scheduled(fixedRateString ="${webapi.ownerTask.rate}", initialDelay=1000)
    public void notificationMeetingOwner() {
        if (!Boolean.parseBoolean(this.environment.getProperty("webapi.ownerTask.enable"))) return;
        this.dbContext.getMeetingRepository().getAll().forEach(item -> {
            var mappedInfo = this.meetingMapper.toDto(item);
            if (!this.publisherService.publishMeetingOwnerMessage(mappedInfo, "Информация о вашем мероприятии")) {

                var errorInfo = String.format("Не удалось отправить сообщение: %s", mappedInfo.getOwnerEmail());
                this.logger.warn(errorInfo);
            }
        });
        this.logger.info("Сообщения разосланы всем владельцам");
    }
}
