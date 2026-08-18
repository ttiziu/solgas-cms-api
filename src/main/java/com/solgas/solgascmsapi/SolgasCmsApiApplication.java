package com.solgas.solgascmsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import com.solgas.solgascmsapi.config.RevalidateProperties;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(RevalidateProperties.class)
public class SolgasCmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolgasCmsApiApplication.class, args);
	}

}
