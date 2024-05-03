package application.domen.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebapiApplication.class, args);
	}
}
