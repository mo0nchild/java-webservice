package application.domen.webapi.services.repository;

import application.domen.webapi.services.repository.infrastructure.IUnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class UnitOfWork implements IUnitOfWork {
    @Autowired
    private final JpaNotificationRepository notificationRepository;
    @Autowired
    private final JpaMeetingRepository meetingRepository;
}
