package application.domen.webapi;

import application.domen.webapi.configurations.TestConfiguration;
import application.domen.webapi.controllers.MeetingController;
import application.domen.webapi.controllers.NotificationController;
import application.domen.webapi.models.requests.NewNotificationInfo;
import application.domen.webapi.models.responses.NotificationInfoResult;
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
import java.util.Objects;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ContextConfiguration(classes=TestConfiguration.class)
public class WebapiApplicationTests {
	private final Logger logger = LoggerFactory.getLogger(WebapiApplicationTests.class);
	@Autowired
	private ApplicationContext context;
	private NotificationController notificationController;
	private MeetingController meetingController;
	@BeforeEach
	protected void initialization() {
		this.notificationController = this.context.getBean(NotificationController.class);
		this.meetingController = this.context.getBean(MeetingController.class);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение всех записей")
	public void controllerGetAllTest() {
		var result = this.notificationController.getAll();
		Assertions.assertThat(result.getBody()).isNotEmpty();
		Assertions.assertThat(result.getBody().size()).isEqualTo(3);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Добавление новой записи")
	public void controllerAddTest() {
		var result = this.notificationController.add(NewNotificationInfo.builder()
				.email("usertest@gmail.com")
				.auditoryId(1)
				.message("Приглашение на встречу")
				.meetingTime(LocalDateTime.now())
				.meetingId(UUID.fromString("15212826-e9e9-4c32-b5c2-f7afe09f9414"))
				.build()
		);
		var checking = this.notificationController.getAll();
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(checking.getBody().size()).isEqualTo(4);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Удаление записи")
	public void controllerDeleteTest() {
		var result = this.notificationController.delete(UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11"));
		var checking = this.notificationController.getAll();
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(checking.getBody().size()).isEqualTo(2);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение по почте")
	public void controllerGetByEmailTest() {
		var result = this.notificationController.getByEmail("user1@gmail.com");
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(result.getBody()).isNotEmpty();

		var mapped = result.getBody().stream().map(NotificationInfoResult::getEmail).toList();
		Assertions.assertThat(mapped).contains("user1@gmail.com");
		Assertions.assertThat(result.getBody().size()).isEqualTo(2);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение по UUID")
	public void controllerGetByUuidTest() {
		var uuid = UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11");
		var result = this.notificationController.getByUuid(uuid);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

		Assertions.assertThat(result.getBody()).isNotNull();
		Assertions.assertThat(result.getBody().getEmail()).isEqualTo("user1@gmail.com");
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение по статусу")
	public void controllerGetByStatusTest() {
		var uuid = UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11");
		var result = this.notificationController.getByStatus("user1@gmail.com", NotificationStatus.CHECKING);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

		var mapped = Objects.requireNonNull(result.getBody()).stream().findFirst();
		Assertions.assertThat(mapped.get()).isNotNull();
		Assertions.assertThat(mapped.get().getNotificationId())
				.isEqualTo(UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11"));
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение по встрече")
	public void controllerGetByMeetingTest() {
		var uuid = UUID.fromString("12ff3af1-d103-433c-a62e-a51560462d63");
		var result = this.meetingController.getByMeeting(uuid);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(result.getBody().size()).isEqualTo(2);

		var mapped = Objects.requireNonNull(result.getBody()).stream().findFirst();
		Assertions.assertThat(mapped.get()).isNotNull();
		Assertions.assertThat(mapped.get().getNotificationId())
				.isEqualTo(UUID.fromString("99049d5e-3d9f-4ca9-b491-48344732ba11"));
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение по статусу встречи")
	public void controllerGetByMeetingStatusTest() {
		var uuid = UUID.fromString("12ff3af1-d103-433c-a62e-a51560462d63");
		var result = this.meetingController.getByMeetingStatus(uuid, NotificationStatus.REJECTED);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(result.getBody().size()).isEqualTo(1);

		var mapped = Objects.requireNonNull(result.getBody()).stream().findFirst();
		Assertions.assertThat(mapped.get()).isNotNull();
		Assertions.assertThat(mapped.get().getNotificationId())
				.isEqualTo(UUID.fromString("d3197142-b4dc-4cb8-931f-ee7281107f8f"));
	}
}
