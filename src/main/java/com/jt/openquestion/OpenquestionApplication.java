package com.jt.openquestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.jt")
@SpringBootApplication
public class OpenquestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenquestionApplication.class, args);
	}

}
