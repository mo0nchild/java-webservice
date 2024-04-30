package application.domen.webapi.controllers;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.requests.UpdateMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.DetailsInfo;
import application.domen.webapi.models.responses.IResponseInfo;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.validation.ValidationEmail;
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
@RequestMapping("/api/meeting")
public class MeetingController {
    private final Logger logger = LoggerFactory.getLogger(MeetingController.class);
    @Autowired
    private final INotificationService notificationService;
    @Autowired
    private final ValidationEmail validationEmail;

    @PostMapping("/add")
    public ResponseEntity<IResponseInfo> add(@RequestBody NewMeetingInfo request) {
        try {
            if (!this.validationEmail.checkValidationRules(request.getOwnerEmail())) {
                throw new NotificationError(String.format("Ошибка валидации: %s", request.getOwnerEmail()));
            };
            for(var email : request.getMembers()) {
                if (!this.validationEmail.checkValidationRules(email)) {
                    throw new NotificationError(String.format("Ошибка валидации: %s", email));
                };
            }
            this.notificationService.addNotification(request);
            return ResponseEntity.ok(new DetailsInfo("Запись успешно добавлена"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @PutMapping("/cancel")
    public ResponseEntity<DetailsInfo> cancel(@RequestParam(value = "uuid") UUID uuid) {
        try {
            this.notificationService.cancelMeeting(uuid);
            return ResponseEntity.ok().body(new DetailsInfo("Мероприятие отменено"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @PutMapping("/update")
    public ResponseEntity<DetailsInfo> update(@RequestBody UpdateMeetingInfo request) {
        try {
            this.notificationService.updateNotification(request);
            return ResponseEntity.ok(new DetailsInfo("Запись успешно изменена"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<IResponseInfo> delete(@RequestParam(value = "uuid") UUID uuid) {
        try {
            this.notificationService.removeMeeting(uuid);
            return ResponseEntity.ok(new DetailsInfo("Запись успешно удалена"));
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(new DetailsInfo(error.getMessage()));
        }
    }
    @GetMapping("/getByUuid")
    public ResponseEntity<IResponseInfo> getByMeeting(@RequestParam UUID uuid) {
        var result = this.notificationService.getByMeeting(uuid);
        return result.<ResponseEntity<IResponseInfo>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(new DetailsInfo("Не удалось найти данные")));
    }
    @GetMapping("/getByStatus")
    public ResponseEntity<IResponseInfo> getByMeetingStatus(
            @RequestParam UUID uuid, @RequestParam NotificationStatus status) {
        var result = this.notificationService.getByMeeting(uuid, status);
        return result.<ResponseEntity<IResponseInfo>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(new DetailsInfo("Не удалось найти данные")));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<MeetingInfoResult>> getAllMeetings() {
        return ResponseEntity.ok().body(this.notificationService.getAllMeetings());
    }
    @GetMapping("/getByOwner")
    public ResponseEntity<List<MeetingInfoResult>> getMeetingByOwner(@RequestParam String email) {
        return ResponseEntity.ok().body(this.notificationService.getByOwnerEmail(email));
    }
}
