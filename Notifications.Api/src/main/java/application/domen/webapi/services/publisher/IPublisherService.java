package application.domen.webapi.services.publisher;

import application.domen.webapi.models.requests.NewNotificationInfo;

import java.util.UUID;

public interface IPublisherService {
    public boolean publishMessage(NewNotificationInfo info, UUID uuid);
}
