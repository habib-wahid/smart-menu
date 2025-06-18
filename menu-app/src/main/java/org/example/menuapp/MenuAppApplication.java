package org.example.menuapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MenuAppApplication{

    public static void main(String[] args) {
        System.out.println("Test work flow for tag");
        SpringApplication.run(MenuAppApplication.class, args);
    }

}
