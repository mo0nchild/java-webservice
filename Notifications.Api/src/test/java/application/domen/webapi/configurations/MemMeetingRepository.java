package application.domen.webapi.configurations;

import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.NotificationEntity;
import application.domen.webapi.services.repository.infrastructure.MeetingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemMeetingRepository implements MeetingRepository {

    private final List<MeetingEntity> memoryData = new ArrayList<>();
    public MemMeetingRepository() { super(); }
    public MemMeetingRepository(List<MeetingEntity> initial) {
        super();
        this.memoryData.addAll(initial);
    }
    @Override
    public Optional<MeetingEntity> getByUUID(UUID meetingId) {
        return this.memoryData.stream().filter(item -> item.getUuid().equals(meetingId)).findFirst();
    }
    @Override
    public List<MeetingEntity> getByOwnerEmail(String email) {
        return this.memoryData.stream().filter(item -> item.getOwnerEmail().equals(email)).toList();
    }
    @Override
    public List<MeetingEntity> getAll() { return this.memoryData; }
    @Override
    public <T extends MeetingEntity> T save(T entity) {
        var result = this.memoryData.add(entity);
        return entity;
    }
    @Override
    public void delete(MeetingEntity entity) {
        var record = this.memoryData.remove(entity);
        if(!record) throw new RuntimeException("Запись не найдена");
    }
}
