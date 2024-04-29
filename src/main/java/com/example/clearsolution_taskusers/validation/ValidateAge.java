package com.example.clearsolution_taskusers.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
@Documented
public @interface ValidateAge {

    String message() default "Age must be greater than or equal to {age.minimum}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
