package com.fooddeliveryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@EnableTransactionManagement
public class FooddeliveryappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FooddeliveryappApplication.class, args);
	}

}
