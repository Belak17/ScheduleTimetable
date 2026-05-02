package com.belak.scheduletimetable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduleTimetableApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleTimetableApplication.class, args);
    }

}
