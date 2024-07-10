package com.ideas2it.cms.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

public class ApiResponseDto<T> extends ResponseEntity<SuccessMessage<T>> {
    private LocalDateTime timestamp;
    private String message;
    private T data;
    private Exception exception;

    private ApiResponseDto(String message, T data, HttpStatus httpStatus) {
        super(new SuccessMessage<>(message, data, httpStatus.value()), httpStatus);
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    private ApiResponseDto(String message, T data, Exception e, HttpStatus httpStatus) {
        super((MultiValueMap<String, String>) new ErrorMessage<>(message, data, httpStatus.value(), e), httpStatus);
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.exception = e;
        this.data = data;
    }
    public static <T> ApiResponseDto<T> statusOk(T data) {
        return new ApiResponseDto<>("Operation successful", data, HttpStatus.OK);
    }

    public static <T> ApiResponseDto<T> statusCreated(T data) {
        return new ApiResponseDto<>("Resource created successfully", data, HttpStatus.CREATED);
    }

    public static <T> ApiResponseDto<T> statusNoContent(T data, Exception e) {
        return new ApiResponseDto<>("No content available", data,e, HttpStatus.NO_CONTENT);
    }

    public static <T> ApiResponseDto<T> statusAccepted(T data) {
        return new ApiResponseDto<>("Request accepted", data, HttpStatus.ACCEPTED);
    }

    public static <T> ApiResponseDto<T> statusBadRequest(T data, Exception e) {
        return new ApiResponseDto<>("Invalid request", data, e,HttpStatus.BAD_REQUEST);
    }

    public static <T> ApiResponseDto<T> of(String message, T data, HttpStatus httpStatus) {
        return new ApiResponseDto<>(message, data, httpStatus);
    }

    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}


