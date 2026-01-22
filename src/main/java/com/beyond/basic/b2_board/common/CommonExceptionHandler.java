package com.beyond.basic.b2_board.common;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

//컨트롤러 어노테이션이 붙어있는 모든 예외를 아래 클래스에서 인터세팅(가로채기)
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegal(IllegalArgumentException e){
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(400)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e){
        e.printStackTrace();
        String errorMessage = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(400)
                .error_message(errorMessage)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notValid(NoSuchElementException e){
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(404)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(dto);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notValid(EntityNotFoundException e){
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(404)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(dto);
    }
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 분기처리 할 거 없어서 가능
    public ResponseEntity<?> exception(Exception e){
        e.printStackTrace();
        CommonErrorDto dto = CommonErrorDto.builder()
                .status_code(500)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dto);
    }
}
