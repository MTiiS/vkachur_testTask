package com.example.clearsolution_taskusers.dto;

import com.example.clearsolution_taskusers.validation.ValidateFilterRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
@ValidateFilterRequest
public class UserFilterRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirthFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirthTo;
}
