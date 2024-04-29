package com.example.clearsolution_taskusers;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClearSolutionTaskUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClearSolutionTaskUsersApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
