package com.uni.bankuni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BankuniApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankuniApplication.class, args);
    }

}
