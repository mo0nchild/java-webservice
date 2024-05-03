package application.domen.webapi;

import application.domen.webapi.configurations.TestConfiguration;
import application.domen.webapi.controllers.MeetingController;
import application.domen.webapi.models.requests.NewMeetingInfo;
import application.domen.webapi.models.requests.UpdateMeetingInfo;
import application.domen.webapi.models.responses.MeetingInfoResult;
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
@ContextConfiguration(classes=TestConfiguration.class)
public class MeetingsControllerTests {
	private final Logger logger = LoggerFactory.getLogger(MeetingsControllerTests.class);
	@Autowired
	private ApplicationContext context;
	private MeetingController meetingController;
	@BeforeEach
	protected void initialization() {
		this.meetingController = this.context.getBean(MeetingController.class);
	}
	@AfterEach
	protected void disposing() { }
	@Tag("PROD")
	@Test
	@DisplayName("Добавление нового мероприятия")
	public void meetingAddTest() {
		var result = this.meetingController.add(NewMeetingInfo.builder()
				.ownerEmail("ownertest@gmail.com")
				.place("Аудитория")
				.name("Тестовое собрание")
				.description("Приглашаем вас на мероприятие")
				.meetingTime(LocalDateTime.now())
				.uuid(UUID.randomUUID())
				.members(new ArrayList<>() {{
					add("usertest1@gmail.com");
					add("usertest32@gmail.com");
				}})
				.build()
		);
		var checking = this.meetingController.getAllMeetings();
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(checking.getBody().size()).isEqualTo(3);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Удаление мероприятия")
	public void meetingDeleteTest() {
		var result = this.meetingController.delete(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"));
		var checking = this.meetingController.getAllMeetings();

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(checking.getBody().size()).isEqualTo(1);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Отмена мероприятия")
	public void meetingCancelTest() {
		var result = this.meetingController.cancel(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"));
		var checking = this.meetingController
				.getByMeeting(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"));

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		var response = (MeetingInfoResult)(Objects.requireNonNull(checking.getBody()));
		Assertions.assertThat(response.getStatus()).isEqualTo(MeetingStatus.CANCELED);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Обновление мероприятия")
	public void meetingUpdateTest() {
		var result = this.meetingController.update(UpdateMeetingInfo.builder()
				.description("Приглашаем вас на мероприятие")
				.meetingTime(LocalDateTime.now())
				.name("Мероприятие отменяется")
				.uuid(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"))
				.place("Улица моисеева").build()
		);
		var checking = this.meetingController
				.getByMeeting(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"));

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		var response = (MeetingInfoResult)(Objects.requireNonNull(checking.getBody()));
		Assertions.assertThat(response.getStatus()).isEqualTo(MeetingStatus.UPDATED);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение мероприятия по UUID")
	public void getMeetingByUUIDTest() {
		var result = this.meetingController.getByMeeting(UUID.fromString("815e7770-b956-443f-a2ba-18bf1dc07867"));
		var response = (MeetingInfoResult)(Objects.requireNonNull(result.getBody()));

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(response).isNotNull();
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение списка по статусу и UUID мероприятия")
	public void getMeetingByTest() {
		var result = this.meetingController.getByMeetingStatus(
				UUID.fromString("63bf3e79-7a16-40ba-b0ee-d74193db081a"),
				NotificationStatus.CHECKING
		);
		var response = (MeetingInfoResult)(Objects.requireNonNull(result.getBody()));

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNotifications().size()).isEqualTo(2);
		for(var item : response.getNotifications()) {
			Assertions.assertThat(item.getStatus()).isEqualTo(NotificationStatus.CHECKING);
		}
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение списка мероприятий")
	public void getAllMeetingsTest() {
		var result = this.meetingController.getAllMeetings();

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(result.getBody()).isNotNull();
		Assertions.assertThat(result.getBody().size()).isEqualTo(2);
	}
	@Tag("PROD")
	@Test
	@DisplayName("Получение списка мероприятий по владельцу")
	public void getMeetingByOwnerTest() {
		var result = this.meetingController.getMeetingByOwner("owner1@gmail.com");

		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		Assertions.assertThat(result.getBody()).isNotNull();
		Assertions.assertThat(result.getBody().size()).isEqualTo(1);
	}
}
