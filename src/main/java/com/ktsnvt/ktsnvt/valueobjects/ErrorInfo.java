package com.ktsnvt.ktsnvt.valueobjects;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorInfo {
    public final String path;
    public final String message;
    public final LocalDateTime timestamp;
    public final Integer status;
    public final String error;


    public ErrorInfo(String path, String message, LocalDateTime timestamp, HttpStatus status){
        this.path = path;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status.value();
        this.error = status.getReasonPhrase();

    }
}
