package application.domen.webapi.configurations;

import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import application.domen.webapi.services.repository.infrastructure.NotificationRepository;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemNotificationRepository implements NotificationRepository {
    private final List<NotificationEntity> memoryData = new ArrayList<>();
    public MemNotificationRepository() { super(); }
    public MemNotificationRepository(List<NotificationEntity> initial) {
        super();
        this.memoryData.addAll(initial);
    }
    public Integer getSize() { return this.memoryData.size(); }
    @Override
    public Optional<NotificationEntity> getById(Integer id) {
        return this.memoryData.stream().filter(item -> item.getId().intValue() == id).findFirst();
    }
    @Override
    public Optional<NotificationEntity> getByUuid(UUID id) {
        return this.memoryData.stream().filter(item -> item.getUuid().equals(id)).findFirst();
    }
    @Override
    public List<NotificationEntity> getAll() { return this.memoryData; }

    @Override
    public List<NotificationEntity> getByEmail(String email, NotificationStatus status) {
        return this.memoryData.stream()
                .filter(item -> item.getEmail().equals(email) && item.getStatus() == status).toList();
    }
    @Override
    public List<NotificationEntity> getByEmail(String email) {
        return this.memoryData.stream().filter(item -> item.getEmail().equals(email)).toList();
    }
    public <T extends NotificationEntity> T save(T entity) {
        var result = this.memoryData.add(entity);
        return entity;
    }
    public void delete(NotificationEntity entity) {
        var record = this.memoryData.remove(entity);
        if(!record) throw new RuntimeException("Запись не найдена");
    }
}
