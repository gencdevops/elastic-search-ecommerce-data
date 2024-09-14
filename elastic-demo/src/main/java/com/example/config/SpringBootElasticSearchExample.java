package com.example.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = { "com.example.controller" })
@Configuration
@EntityScan(basePackages = { "com.example.model" })
@ComponentScan(basePackages = { "com.example" })
@EnableJpaRepositories("com.example.repository")
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringBootElasticSearchExample {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootElasticSearchExample.class, args);
	}

}
