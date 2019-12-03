package com.jt.openquestion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@ComponentScan("com.jt.openquestion")
@EnableResourceServer
//@MapperScan(value = {"com.jt.openquestion.mapper","com.lmash.mysqltest.generator.mapper"})
@SpringBootApplication
public class OpenquestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenquestionApplication.class, args);
	}

}
