package com.manda.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Agenda1Application {

	public static void main(String[] args) {
		SpringApplication.run(Agenda1Application.class, args);
	}

}
