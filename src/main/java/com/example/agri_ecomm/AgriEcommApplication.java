package com.example.agri_ecomm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.agri_ecomm", "com.example.productcatalog"})
@EntityScan(basePackages = "com.example.productcatalog.model")
@EnableJpaRepositories(basePackages = "com.example.productcatalog.repository")
public class AgriEcommApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriEcommApplication.class, args);
    }
}
