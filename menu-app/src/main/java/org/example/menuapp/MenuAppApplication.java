package org.example.menuapp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.PlatformTransactionManager;

@EnableCaching
@SpringBootApplication
public class MenuAppApplication{

    @Autowired
    private PlatformTransactionManager transactionManager;

    public static void main(String[] args) {
        SpringApplication.run(MenuAppApplication.class, args);
    }

    @PostConstruct
    public void logTransactionManager() {
        System.out.println("Using transaction manager: " + transactionManager.getClass().getName());
    }

}
