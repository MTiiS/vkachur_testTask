package com.example.clearsolution_taskusers.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetail {
    private int status;
    private String detail;

}
