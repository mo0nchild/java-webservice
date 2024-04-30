package application.domen.webapi.services.publisher;

import application.domen.webapi.models.commons.NotificationInfo;
import application.domen.webapi.models.responses.NotificationInfoResult;

import java.util.UUID;

public interface IPublisherService {
    public boolean publishInviteMessage(NotificationInfoResult info, String title);
    public boolean publishUpdateMessage(NotificationInfoResult info, String title);
}
