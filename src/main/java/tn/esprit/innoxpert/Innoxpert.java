package tn.esprit.innoxpert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import tn.esprit.innoxpert.Util.EmailClass;

@SpringBootApplication
@EnableScheduling
public class Innoxpert {

	public static void main(String[] args) {
		SpringApplication.run(Innoxpert.class, args);


	}

}
