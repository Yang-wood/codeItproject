package com.codeit.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.codeit.mini.repository.vending.querydsl.ICouponStatusRepository;

@SpringBootApplication
@EnableJpaAuditing
public class CodeItApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeItApplication.class, args);
	}

}
