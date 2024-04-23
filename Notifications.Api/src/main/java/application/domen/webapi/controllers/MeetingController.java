package application.domen.webapi.controllers;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    private final Logger logger = LoggerFactory.getLogger(MeetingController.class);
    @Autowired
    private final TemplateEngine templateEngine;
    @Autowired
    private final INotificationService notificationService;
    @GetMapping("/getByUuid")
    public ResponseEntity<List<NotificationInfo>> getByMeeting(@RequestParam UUID uuid) {
        return ResponseEntity.ok(this.notificationService.getByMeeting(uuid));
    }
    @GetMapping("/getByStatus")
    public ResponseEntity<List<NotificationInfo>> getByMeetingStatus(
            @RequestParam UUID uuid, @RequestParam NotificationStatus status) {
        return ResponseEntity.ok(this.notificationService.getByMeeting(uuid, status));
    }
    @GetMapping("/status")
    public ResponseEntity<String> changeStatus(@RequestParam NotificationStatus status, @RequestParam UUID uuid) {
        var context = new Context();
        context.setVariable("status", status == NotificationStatus.ACCEPTED ? "принято" : "отклонено");
        var content = templateEngine.process("status-template", context);
        try {
            this.notificationService.changeStatus(new ChangeStatusInfo(uuid, status));
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
}
