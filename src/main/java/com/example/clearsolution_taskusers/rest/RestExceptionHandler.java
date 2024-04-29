package com.example.clearsolution_taskusers.rest;

import com.example.clearsolution_taskusers.dto.error.ErrorDetail;
import com.example.clearsolution_taskusers.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorDetail(HttpStatus.BAD_REQUEST.value(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(new ErrorDetail(HttpStatus.NOT_FOUND.value(), "Element not found: " + ex.getMessage()));
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), "Invalid JSON format" + ex));
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(new ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error: " + ex.getMessage()));
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}