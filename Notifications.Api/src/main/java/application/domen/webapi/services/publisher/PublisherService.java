package application.domen.webapi.services.publisher;

import application.domen.webapi.models.commons.MeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Setter
@Slf4j
public class PublisherService implements IPublisherService {
    private final Logger logger = LoggerFactory.getLogger(PublisherService.class);
    @Autowired
    private final JavaMailSender emailSender;
    @Autowired
    private final TemplateEngine templateEngine;
    @Autowired
    private final Environment environment;
    private final Context buildContext(MeetingInfo info, UUID uuid, String title, boolean actions) {
        var context = new Context();
        context.setVariable("message", info.getDescription().isEmpty()
                ? "Мероприятие проводится на базе общей платформы ВГТУ" : info.getDescription());
        context.setVariable("title", title);
        context.setVariable("time", info.getMeetingTime());
        context.setVariable("name", info.getName());
        context.setVariable("place", info.getPlace());
        context.setVariable("actions", actions);
        if(!actions) return context;

        var address = this.environment.getProperty("webapi.publish.ip", String.class);
        var acceptUrl = String.format("http://%s:8080/api/status?status=%s&uuid=%s",
                address, NotificationStatus.ACCEPTED, uuid);
        var rejectUrl = String.format("http://%s:8080/api/status?status=%s&uuid=%s",
                address, NotificationStatus.REJECTED, uuid);
        this.logger.info(acceptUrl);
        context.setVariable("acceptUrl", acceptUrl);
        context.setVariable("rejectUrl", rejectUrl);
        return context;
    }
    @Override
    public boolean publishInviteMessage(NotificationInfoResult info, String title) {
        var mimeMessage = emailSender.createMimeMessage();
        var publishing = this.environment.getProperty("webapi.publish.enabled", Boolean.class);
        try {
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8") {{
                setTo(info.getEmail());
                setSubject("Приглашение на встречу");
            }};
            var context = this.buildContext(info.getMeeting(), info.getUuid(), title, true);
            helper.setText(templateEngine.process("email-template", context), true);
            helper.setFrom(Objects.requireNonNull(this.environment.getProperty("spring.mail.username")));
            this.emailSender.send(mimeMessage);
        }
        catch(Exception error) {
            System.out.println(error.getMessage());
            return !Boolean.TRUE.equals(publishing);
        }
        return true;
    }
    @Override
    public boolean publishUpdateMessage(NotificationInfoResult info, String title) {
        var mimeMessage = emailSender.createMimeMessage();
        var publishing = this.environment.getProperty("webapi.publish.enabled", Boolean.class);
        try {
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8") {{
                setTo(info.getEmail());
                setSubject(title);
            }};
            var context = this.buildContext(info.getMeeting(), info.getUuid(), title, false);
            helper.setText(templateEngine.process("email-template", context), true);
            helper.setFrom(Objects.requireNonNull(this.environment.getProperty("spring.mail.username")));
            this.emailSender.send(mimeMessage);
        }
        catch(Exception error) {
            System.out.println(error.getMessage());
            return !Boolean.TRUE.equals(publishing);
        }
        return true;
    }
    @Override
    public boolean publishMeetingOwnerMessage(MeetingInfoResult info, String title) {
        var mimeMessage = emailSender.createMimeMessage();
        var publishing = this.environment.getProperty("webapi.publish.enabled", Boolean.class);
        try {
            var acceptedCount = info.getNotifications().stream()
                    .filter(item -> item.getStatus() == NotificationStatus.ACCEPTED).count();
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8") {{
                setTo(info.getOwnerEmail());
                setSubject(title);
            }};
            var context = new Context();
            context.setVariable("title", "Информация о вашем мероприятии");
            context.setVariable("name", info.getName());
            context.setVariable("message", info.getDescription());
            context.setVariable("userCount", acceptedCount);
            helper.setText(templateEngine.process("owner-template", context), true);
            helper.setFrom(Objects.requireNonNull(this.environment.getProperty("spring.mail.username")));
            this.emailSender.send(mimeMessage);
        }
        catch(Exception error) {
            System.out.println(error.getMessage());
            return !Boolean.TRUE.equals(publishing);
        }
        return true;
    }
}
