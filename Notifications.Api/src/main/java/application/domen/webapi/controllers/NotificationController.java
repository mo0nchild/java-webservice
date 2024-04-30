package application.domen.webapi.controllers;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.responses.DetailsInfo;
import application.domen.webapi.models.responses.IResponseInfo;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.validation.ValidationEmail;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private final INotificationService notificationService;
    @Autowired
    private final TemplateEngine templateEngine;

    @DeleteMapping("/delete")
    public ResponseEntity<IResponseInfo> delete(@RequestParam(value = "uuid") UUID uuid) {
        try {
            this.notificationService.removeNotification(uuid);
            return ResponseEntity.ok(new DetailsInfo("Запись успешно удалена"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @GetMapping("/getByUuid")
    public ResponseEntity<IResponseInfo> getByUuid(@RequestParam(value = "uuid") UUID uuid) {
        var result = this.notificationService.getByUuid(uuid);
        return result.<ResponseEntity<IResponseInfo>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(new DetailsInfo("Не удалось найти данные")));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationInfoResult>> getAll() {
        return ResponseEntity.ok(this.notificationService.getAll());
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<List<NotificationInfoResult>> getByEmail(
            @RequestParam(value = "email") String email) {
        return ResponseEntity.ok(this.notificationService.getAllByEmail(email));
    }
    @GetMapping("/getByStatus")
    public ResponseEntity<List<NotificationInfoResult>> getByStatus(
            @RequestParam String email, @RequestParam NotificationStatus status) {
        return ResponseEntity.ok(this.notificationService.getAllByEmail(email, status));
    }
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsInfo> changeStatus(@RequestParam NotificationStatus status,
                                                    @RequestParam UUID uuid) {
        try {
            this.notificationService.changeStatus(new ChangeStatusInfo(uuid, status));
            return ResponseEntity.ok().body(new DetailsInfo("Статус успешно изменен"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @GetMapping(value = "/status", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> changeStatusHTML(@RequestParam NotificationStatus status,
                                                   @RequestParam UUID uuid) {
        var context = new Context();
        var title = String.format("Приглашение на встречу %s", status == NotificationStatus.ACCEPTED ? "принято"
                        : (status == NotificationStatus.REJECTED ? "отклонено" : "обработано"));
        context.setVariable("status", title);
        try {
            var content = templateEngine.process("status-template", context);
            this.notificationService.changeStatus(new ChangeStatusInfo(uuid, status));

            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
        }
        catch (NotificationError error) {
            context.setVariable("status", error.getMessage());
            return ResponseEntity.badRequest().body(templateEngine.process("status-template", context));
        }
    }
}
