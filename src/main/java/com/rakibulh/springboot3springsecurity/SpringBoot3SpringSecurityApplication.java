package com.rakibulh.springboot3springsecurity;

import com.rakibulh.springboot3springsecurity.initialize.InitializeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class SpringBoot3SpringSecurityApplication implements CommandLineRunner {

    @Autowired
    private InitializeData initializeData;

    public static void main(String[] args) {
//        SpringApplication.run(SpringBoot3SpringSecurityApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(SpringBoot3SpringSecurityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initializeData.initialize();
    }
}
