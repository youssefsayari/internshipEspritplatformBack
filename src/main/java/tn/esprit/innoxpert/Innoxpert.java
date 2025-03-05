package tn.esprit.innoxpert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Innoxpert {

	public static void main(String[] args) {
		SpringApplication.run(Innoxpert.class, args);
	//	new EmailClass().sendMeetingReminder("kthiri1919@gmail.com", "John Doe", "Jane Smith", "2024-03-15", "14:30", "https://meet.jit.si/MyMeeting");

	}

}
