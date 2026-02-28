package org.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // This tells Spring to look for @Scheduled tasks
public class BatchApplication {

    public static void main(String[] args) {

        SpringApplication.run(BatchApplication.class, args);
    }

}
