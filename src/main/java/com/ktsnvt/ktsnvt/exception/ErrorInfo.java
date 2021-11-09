package com.ktsnvt.ktsnvt.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorInfo {
    public final String path;
    public final String message;
    public final LocalDateTime timestamp;
    public final Integer statusCode;
    public final String statusReason;
    public final Map<String, String> errors;


    public ErrorInfo(String path, String message, LocalDateTime timestamp, HttpStatus statusCode) {
        this.path = path;
        this.message = message;
        this.timestamp = timestamp;
        this.statusCode = statusCode.value();
        this.statusReason = statusCode.getReasonPhrase();
        this.errors = new HashMap<>();
    }

    public ErrorInfo(String path, String message, LocalDateTime timestamp, HttpStatus statusCode, Map<String, String> errors) {
        this.path = path;
        this.message = message;
        this.timestamp = timestamp;
        this.statusCode = statusCode.value();
        this.statusReason = statusCode.getReasonPhrase();
        this.errors = errors;
    }
}
