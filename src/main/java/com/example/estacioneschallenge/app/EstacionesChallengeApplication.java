package com.example.estacioneschallenge.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.estacioneschallenge.controller", "com.example.estacioneschallenge.service"})
public class EstacionesChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstacionesChallengeApplication.class, args);
    }

}
