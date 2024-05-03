package application.domen.webapi.services.repository.infrastructure;

import application.domen.webapi.services.repository.JpaMeetingRepository;
import application.domen.webapi.services.repository.JpaNotificationRepository;

public interface IUnitOfWork {
    public NotificationRepository getNotificationRepository();
    public MeetingRepository getMeetingRepository();
}
