package application.domen.webapi.controllers;

import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.mapper.NotificationMapper;
import application.domen.webapi.services.notification.INotificationService;
import application.domen.webapi.services.notification.NotificationService;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private final INotificationService notificationService;
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody NewNotificationInfo request) {
        try {
            this.notificationService.addNotification(request);
            return ResponseEntity.ok("Запись успешно добавлена");
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "uuid") UUID uuid) {
        try {
            this.notificationService.removeNotification(uuid);
            return ResponseEntity.ok("Запись успешно удалена");
        }
        catch (NotificationError error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
    @GetMapping("/getByUuid")
    public ResponseEntity<NotificationInfo> getByEmail(@RequestParam(value = "uuid") UUID uuid) {
        var result = this.notificationService.getByUuid(uuid);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationInfo>> getAll() {
        return ResponseEntity.ok(this.notificationService.getAll());
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<List<NotificationInfo>> getByEmail(
            @RequestParam(value = "email") String email) {
        return ResponseEntity.ok(this.notificationService.getAllByEmail(email));
    }
}
