package com.example.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@EnableBatchProcessing
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:" + 8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
