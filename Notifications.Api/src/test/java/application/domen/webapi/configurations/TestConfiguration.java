package application.domen.webapi.configurations;

import application.domen.webapi.controllers.MeetingController;
import application.domen.webapi.controllers.NotificationController;
import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.services.mappers.notification.NotificationMapper;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.NotificationService;
import application.domen.webapi.services.publisher.IPublisherService;
import application.domen.webapi.services.repository.infrastructure.NotificationRepository;
import application.domen.webapi.services.validation.ValidationEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
@ComponentScan
@Import(value = TemplateEngine.class)
public class TestConfiguration {
    @Autowired
    private TemplateEngine templateEngine;
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public NotificationRepository notificationRepository() {
        return new MemNotificationRepository(new ArrayList<>(){{
            addAll(DatabaseEntities.generateEntities());
        }});
    }
    @Bean
    public IPublisherService publisherService(ApplicationContext context) {
        return new IPublisherService() {
            @Override
            public boolean publishMessage(NewNotificationInfo info, UUID uuid) {
                return true;
            }
        };
    }
    @Bean
    public NotificationMapper notificationMapper() { return new NotificationMapper(); }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public INotificationService notificationService(ApplicationContext context) {
        return new NotificationService(
                context.getBean(NotificationRepository.class),
                context.getBean(NotificationMapper.class),
                context.getBean(IPublisherService.class),
                context.getEnvironment()
        );
    }
    @Bean
    public ValidationEmail validationEmail() { return new ValidationEmail(); }
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NotificationController notificationController(INotificationService notificationService,
                                                         ValidationEmail validationEmail) {
        return new NotificationController(notificationService, validationEmail);
    }
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MeetingController meetingController(INotificationService notificationService) {
        return new MeetingController(this.templateEngine, notificationService);
    }
}
