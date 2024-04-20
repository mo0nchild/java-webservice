package application.domen.webapi.services.publisher;

import application.domen.webapi.controllers.NotificationController;
import application.domen.webapi.models.requests.NewNotificationInfo;
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
    private final Context buildContext(NewNotificationInfo info, UUID uuid) {
        var context = new Context();
        context.setVariable("message", info.getMessage().isEmpty()
                ? "Мероприятие проводится на базе общей платформы ВГТУ" : info.getMessage());
        context.setVariable("time", info.getMeetingTime());
        context.setVariable("place", info.getAuditoryId());

        var address = this.environment.getProperty("webapi.publish.ip", String.class);
        var url = String.format("http://%s:8080/api/meeting/status?status=%s&uuid=%s", address, true, uuid);
        this.logger.info(url);
        context.setVariable("url", url);
        return context;
    }
    @Override
    public boolean publishMessage(NewNotificationInfo info, UUID uuid) {
        var mimeMessage = emailSender.createMimeMessage();
        var publishing = this.environment.getProperty("webapi.publish.enabled", Boolean.class);
        try {
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8") {{
                setTo(info.getEmail());
                setSubject("Приглашение на встречу");
            }};
            helper.setText(templateEngine.process("email-template", this.buildContext(info, uuid)), true);
            helper.setFrom(Objects.requireNonNull(this.environment.getProperty("spring.mail.username")));
            this.emailSender.send(mimeMessage);
        }
        catch(Exception error) { return !Boolean.TRUE.equals(publishing); }
        return true;
    }
}
