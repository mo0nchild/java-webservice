package application.domen.webapi.services.notification;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.responses.NotificationInfo;
import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.entities.NotificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INotificationService {
    public void addNotification(NewNotificationInfo info) throws NotificationError;
    public void removeNotification(UUID uuid) throws NotificationError;
    public void changeStatus(ChangeStatusInfo status) throws NotificationError;

    public Optional<NotificationInfo> getByUuid(UUID id);
    public List<NotificationInfo> getAllByEmail(String email);
    public List<NotificationInfo> getAllByEmail(String email, NotificationStatus status);
    public List<NotificationInfo> getAll();
    public List<NotificationInfo> getByMeeting(UUID uuid);
    public List<NotificationInfo> getByMeeting(UUID uuid, NotificationStatus status);
}
