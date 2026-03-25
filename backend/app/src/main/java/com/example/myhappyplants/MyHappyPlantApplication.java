package com.example.myhappyplants;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class MyHappyPlantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyHappyPlantApplication.class, args);
    }

}
