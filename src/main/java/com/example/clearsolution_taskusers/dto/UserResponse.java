package com.example.clearsolution_taskusers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserResponse<T> {
    private T data;
}
