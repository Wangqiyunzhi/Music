package com.rabbiter.music.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e) {
        String message = (e == null) ? "internal server error" : e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = (e == null) ? "internal server error" : e.getClass().getSimpleName();
        }

        if (message.contains("(using password: YES)")) {
            if (!message.contains("'root'@'")) {
                message = "database authentication failed";
            } else if (message.contains("'root'@'localhost'")) {
                message = "database password error";
            }
        } else if (message.contains("Table") && message.contains("doesn't exist")) {
            message = "database table not found";
        } else if (message.contains("Unknown database")) {
            message = "database not found";
        } else if (message.contains("edis")) {
            message = "redis connection failed";
        } else if (message.contains("Failed to obtain JDBC Connection")) {
            message = "database connection failed";
        } else if (message.contains("SQLSyntaxErrorException")) {
            message = "sql syntax error";
        }

        if (e != null) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
