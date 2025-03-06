package org.example.menuapp;

import org.springframework.boot.SpringApplication;

public class TestMenuAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(MenuAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
