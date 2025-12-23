package com.example.notesApp.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private String path;
    private LocalDateTime errorTime;
}
