package com.example.projectrestfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProjectRestFulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectRestFulApiApplication.class, args);
    }

}
