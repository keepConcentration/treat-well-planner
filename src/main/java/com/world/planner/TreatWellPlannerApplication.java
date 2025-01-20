package com.world.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TreatWellPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreatWellPlannerApplication.class, args);
	}

}
