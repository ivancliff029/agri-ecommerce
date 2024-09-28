package com.example.agri_ecomm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.productcatalog.repository")
public class AgriEcommApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriEcommApplication.class, args);
    }
}
