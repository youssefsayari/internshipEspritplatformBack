package tn.esprit.innoxpert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.esprit.innoxpert.Util.JitsiMeetingService;

@SpringBootApplication
public class Innoxpert {

	public static void main(String[] args) {
		SpringApplication.run(Innoxpert.class, args);
		System.out.println(new JitsiMeetingService().generateMeetingLink("res"));

	}

}
