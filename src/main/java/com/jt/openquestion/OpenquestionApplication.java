package com.jt.openquestion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.jt")
//@MapperScan(value = {"com.jt.openquestion.mapper","com.lmash.mysqltest.generator.mapper"})
@SpringBootApplication
public class OpenquestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenquestionApplication.class, args);
	}

}
