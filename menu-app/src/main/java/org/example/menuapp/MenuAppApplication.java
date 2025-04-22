package org.example.menuapp;

import org.example.menuapp.config.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MenuAppApplication{

    public static void main(String[] args) {
        SpringApplication.run(MenuAppApplication.class, args);
    }

}
