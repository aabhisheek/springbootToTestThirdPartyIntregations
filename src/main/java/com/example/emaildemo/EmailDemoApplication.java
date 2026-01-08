package com.example.emaildemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailDemoApplication.class, args);
        System.out.println("\n" +
                "==============================================\n" +
                "  Spring Boot Email Demo Application Started!\n" +
                "  Port: 8080\n" +
                "  H2 Console: http://localhost:8080/h2-console\n" +
                "==============================================\n");
    }
}
