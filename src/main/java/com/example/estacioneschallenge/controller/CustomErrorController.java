package com.example.estacioneschallenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){

        List errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity handleTypeErrors(MethodArgumentTypeMismatchException exception){

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(exception.getPropertyName(), exception.getMessage());

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity handleTypeErrors(HttpMessageNotReadableException exception){

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", "Request contains at least one invalid data type");

        return ResponseEntity.badRequest().body(errorMap);
    }
}
