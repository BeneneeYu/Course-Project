package com.ooad.lab2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;

@SpringBootApplication
@EnableJpaRepositories("com.ooad.lab2.repository")
public class Lab2Application {
    private Mask mask;

    @Autowired
    public Lab2Application(Mask mask){this.mask = mask;}

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner dataLoader() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                mask.initialize();
            }
        };
    }
}
