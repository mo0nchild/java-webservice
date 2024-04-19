package application.domen.webapi.controllers;

import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.services.mapper.NotificationMapper;
import application.domen.webapi.services.repository.NotificationRepository;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private NotificationMapper mapper;
    @GetMapping("/getall")
    public ResponseEntity<List<NotificationInfo>> getAll(@RequestParam(value = "name", defaultValue = "") String name) {
        var result = this.repository.getAll()
                .stream()
                .map(item -> this.mapper.toDto(item))
                .toList();

        return ResponseEntity.ok(result);
    }
}
