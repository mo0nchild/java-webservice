package application.domen.webapi.configurations;

import application.domen.webapi.controllers.MeetingController;
import application.domen.webapi.controllers.NotificationController;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.mappers.meeting.IMeetingMapper;
import application.domen.webapi.services.mappers.meeting.MeetingMapper;
import application.domen.webapi.services.mappers.notification.INotificationMapper;
import application.domen.webapi.services.mappers.notification.NotificationMapper;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.NotificationService;
import application.domen.webapi.services.publisher.IPublisherService;
import application.domen.webapi.services.repository.JpaMeetingRepository;
import application.domen.webapi.services.repository.JpaNotificationRepository;
import application.domen.webapi.services.repository.infrastructure.IUnitOfWork;
import application.domen.webapi.services.repository.infrastructure.MeetingRepository;
import application.domen.webapi.services.repository.infrastructure.NotificationRepository;
import application.domen.webapi.services.validation.ValidationEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
@Import(value = {TemplateEngine.class})
public class TestConfiguration {
    @Autowired
    private TemplateEngine templateEngine;
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public NotificationRepository notificationRepository() {
        DatabaseEntities.generateEntities();
        return new MemNotificationRepository(new ArrayList<>(){{
            addAll(DatabaseEntities.getNotifications());
        }});
    }
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public MeetingRepository meetingRepository() {
        DatabaseEntities.generateEntities();
        return new MemMeetingRepository(new ArrayList<>(){{
            addAll(DatabaseEntities.getMeetings());
        }});
    }
    @Bean
    public IPublisherService publisherService(ApplicationContext context) {
        return new IPublisherService() {
            @Override
            public boolean publishInviteMessage(NotificationInfoResult info, String title) { return true; }
            @Override
            public boolean publishUpdateMessage(NotificationInfoResult info, String title) { return true; }
            @Override
            public boolean publishMeetingOwnerMessage(MeetingInfoResult info, String title) { return true;}
        };
    }
    @Bean
    public INotificationMapper notificationMapper() { return new NotificationMapper(); }
    @Bean
    public IMeetingMapper meetingMapper() { return new MeetingMapper(); }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public IUnitOfWork unitOfWork(ApplicationContext context) {
        return new IUnitOfWork() {
            @Override
            public NotificationRepository getNotificationRepository() {
                return context.getBean(NotificationRepository.class);
            }
            @Override
            public MeetingRepository getMeetingRepository() {
                return context.getBean(MeetingRepository.class);
            }
        };
    }
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public INotificationService notificationService(ApplicationContext context) {
        return new NotificationService(
                context.getBean(IUnitOfWork.class),
                context.getBean(NotificationMapper.class),
                context.getBean(MeetingMapper.class),
                context.getBean(IPublisherService.class)
        );
    }
    @Bean
    public ValidationEmail validationEmail() { return new ValidationEmail(); }
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public NotificationController notificationController(INotificationService notificationService) {
        return new NotificationController(notificationService, this.templateEngine);
    }
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public MeetingController meetingController(INotificationService notificationService,
                                               ValidationEmail validationEmail) {
        return new MeetingController(notificationService, validationEmail);
    }
}
