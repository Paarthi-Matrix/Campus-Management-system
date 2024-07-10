package com.ideas2it.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorMessage<T> {
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    @NotNull
    private Exception e;
    private T data;

    private ErrorMessage() {
        timestamp = LocalDateTime.now();
    }

    ErrorMessage(int status) {
        this();
        this.status = status;
    }

    ErrorMessage(int status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ErrorMessage(int status, String message, Throwable e) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = e.getLocalizedMessage();
    }

    ErrorMessage(String message, T data, int status, Exception e) {
        this();
        this.status = status;
        this.message = e.getMessage();
        this.e = e;
    }
}

