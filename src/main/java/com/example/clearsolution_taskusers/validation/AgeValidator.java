package com.example.clearsolution_taskusers.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
@PropertySource("classpath:validationValues.properties")
public class AgeValidator implements ConstraintValidator<ValidateAge, LocalDate> {

    @Value("${age.minimum}")
    private int minimumAge;

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirth == null) {
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears() >= minimumAge;
    }
}
