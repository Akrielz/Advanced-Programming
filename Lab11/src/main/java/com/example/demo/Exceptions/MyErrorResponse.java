package com.example.demo.Exceptions;

import java.time.LocalDateTime;

public class MyErrorResponse {
    private LocalDateTime date;
    private String message;

    public MyErrorResponse(String message) {
        this.message = "Error: " + message;
    }

    public void setTimestamp(LocalDateTime now) {
        date = now;
    }
}
