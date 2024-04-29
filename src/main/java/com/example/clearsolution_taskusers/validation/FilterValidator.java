package com.example.clearsolution_taskusers.validation;

import com.example.clearsolution_taskusers.dto.UserFilterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FilterValidator implements ConstraintValidator <ValidateFilterRequest, UserFilterRequest> {
    @Override
    public boolean isValid(UserFilterRequest userFilterRequest, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (userFilterRequest == null) {
           return true;
       }

        LocalDate fromDate = userFilterRequest.getDateOfBirthFrom();
        LocalDate toDate = userFilterRequest.getDateOfBirthTo();

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Start date must be before end date")
                    .addConstraintViolation();
            return false;
        }

        return isValid;
    }
}
