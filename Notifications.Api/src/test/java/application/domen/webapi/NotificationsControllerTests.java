package application.domen.webapi;

import application.domen.webapi.configurations.TestConfiguration;
import application.domen.webapi.controllers.MeetingController;
import application.domen.webapi.controllers.NotificationController;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.requests.UpdateMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
import application.domen.webapi.models.responses.NotificationInfoResult;
import application.domen.webapi.services.repository.entities.MeetingStatus;
import application.domen.webapi.services.repository.entities.NotificationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
public class NotificationsControllerTests {
    private final Logger logger = LoggerFactory.getLogger(NotificationsControllerTests.class);
    @Autowired
    private ApplicationContext context;
    private NotificationController notificationController;
    @BeforeEach
    protected void initialization() {
        this.notificationController = this.context.getBean(NotificationController.class);
    }
    @AfterEach
    protected void disposing() { }
    @Tag("PROD")
    @Test
    @DisplayName("Удаление уведомления")
    public void deleteNotificationTest() {
        var result = this.notificationController.delete(UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366"));
        var checking = this.notificationController.getAll();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(checking.getBody().size()).isEqualTo(5);
    }
    @Tag("PROD")
    @Test
    @DisplayName("Получение уведомления по UUID")
    public void getByUuidTest() {
        var result = this.notificationController.getByUuid(UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366"));
        var response = (NotificationInfoResult)result.getBody();

        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(response.getUuid()).isEqualTo(UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366"));
    }
    @Tag("PROD")
    @Test
    @DisplayName("Получение списка уведомлений")
    public void getAllTest() {
        var result = this.notificationController.getAll();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().size()).isEqualTo(6);
    }
    @Tag("PROD")
    @Test
    @DisplayName("Получение списка уведомлений по email")
    public void getByEmailTest() {
        var result = this.notificationController.getByEmail("user2@gmail.com");
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().size()).isEqualTo(2);
    }
    @Tag("PROD")
    @Test
    @DisplayName("Получение списка уведомлений по email и статусу")
    public void getByStatusTest() {
        var result = this.notificationController.getByStatus("user2@gmail.com", NotificationStatus.ACCEPTED);
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().size()).isEqualTo(1);
        for(var item : result.getBody()) {
            Assertions.assertThat(item.getStatus()).isEqualTo(NotificationStatus.ACCEPTED);
        }
    }
    @Tag("PROD")
    @Test
    @DisplayName("Изменение статуса уведомления")
    public void changeStatusTest() {
        var result = this.notificationController.changeStatus(
                NotificationStatus.ACCEPTED,
                UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366")
        );
        var checking = this.notificationController.getByUuid(UUID.fromString("d100be5d-1ca5-4738-bdad-a2a29af3c366"));
        var response = (NotificationInfoResult)checking.getBody();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(NotificationStatus.ACCEPTED);
    }
}

