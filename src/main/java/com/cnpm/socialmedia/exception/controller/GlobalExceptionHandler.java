package com.cnpm.socialmedia.exception.controller;


import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    private ResponseEntity<?> handleException(AppException e){
        return ResponseEntity.status(e.getCode())
                .body(new ResponseDTO(false,e.getMessage(),null));
    }
    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<?> handleExceptionLogin(BadCredentialsException e){
        return ResponseEntity.status(403)
                .body(new ResponseDTO(false,"Email or password incorrect",null));
    }
}
