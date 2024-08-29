package com.sl.tdbms.web.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
//@PropertySource(value = "classpath:application-dev.yml",ignoreResourceNotFound = true)
public class AdminApplication {

    public static void main(String[] args) {
        //SpringApplication.run(AdminApplication.class, args);
        SpringApplication app = new SpringApplication(AdminApplication.class);
        // Set default to 'prod' if no other profile is set
        String[] activeProfiles = System.getProperty("spring.profiles.active", "dev").split(",");
        app.setAdditionalProfiles(activeProfiles);
        app.run(args);
    } 
}
