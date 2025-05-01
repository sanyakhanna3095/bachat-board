package com.cg.budgetboard.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        List<String> errorMessage = errorList.stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
        return new ResponseEntity<String>(String.valueOf(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e){
        String message = e.getMessage();
        return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
    }
}