package application.domen.webapi.services.notification;

import application.domen.webapi.models.requests.ChangeStatusInfo;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.requests.UpdateMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.notification.infastructure.NotificationError;
import application.domen.webapi.services.repository.entities.MeetingEntity;
import application.domen.webapi.services.repository.entities.NotificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INotificationService {
    public void addNotification(NewMeetingInfo info) throws NotificationError;
    public void updateNotification(UpdateMeetingInfo info) throws NotificationError;
    public void removeNotification(UUID uuid) throws NotificationError;
    public void removeMeeting(UUID uuid) throws NotificationError;

    public void cancelMeeting(UUID uuid) throws NotificationError;
    public void changeStatus(ChangeStatusInfo status) throws NotificationError;

    public Optional<NotificationInfoResult> getByUuid(UUID id);
    public List<NotificationInfoResult> getAllByEmail(String email);
    public List<NotificationInfoResult> getAllByEmail(String email, NotificationStatus status);
    public List<NotificationInfoResult> getAll();

    public List<MeetingInfoResult> getByOwnerEmail(String email);
    public List<MeetingInfoResult> getAllMeetings();
    public Optional<MeetingInfoResult> getByMeeting(UUID uuid);
    public Optional<MeetingInfoResult> getByMeeting(UUID uuid, NotificationStatus status);
}
