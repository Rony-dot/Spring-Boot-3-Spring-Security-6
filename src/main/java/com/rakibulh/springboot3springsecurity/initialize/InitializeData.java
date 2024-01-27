package com.rakibulh.springboot3springsecurity.initialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakibulh.springboot3springsecurity.model.RegisterRequest;
import com.rakibulh.springboot3springsecurity.repositories.UserRepository;
import com.rakibulh.springboot3springsecurity.services.AuthenticationService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InitializeData {
    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private ResourceLoader resourceLoader;

    public InitializeData(AuthenticationService authenticationService, UserRepository userRepository, ResourceLoader resourceLoader) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
    }

    public void initialize(){
        registerUsers();
    }

    private void registerUsers(){
        userRepository.deleteAll();
        try{
            var userModels = new ObjectMapper()
                    .findAndRegisterModules()
                    .readValue(
                            resourceLoader.getResource("classpath:users.json").getInputStream(),
                            new TypeReference<ArrayList<RegisterRequest>>() {
                            }
                    );
            userModels.forEach(userModel -> authenticationService.register(userModel));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
