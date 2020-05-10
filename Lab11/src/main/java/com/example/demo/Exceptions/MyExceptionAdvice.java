package com.example.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MyExceptionAdvice {

    @ExceptionHandler(value = ExceptionPlayerNotFound.class)
    public ResponseEntity<MyErrorResponse> handlePlayer(ExceptionPlayerNotFound e) {
        MyErrorResponse error = new MyErrorResponse("PlayerNotFound");
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExceptionGameNotFound.class)
    public ResponseEntity<MyErrorResponse> handleGame(ExceptionGameNotFound e) {
        MyErrorResponse error = new MyErrorResponse("GameNotFound");
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExceptionColor.class)
    public ResponseEntity<MyErrorResponse> handleColor(ExceptionColor e) {
        MyErrorResponse error = new MyErrorResponse("Bad color. Available colors: {0, 1}");
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExceptionBoardLength.class)
    public ResponseEntity<MyErrorResponse> handleBoard(ExceptionBoardLength e) {
        MyErrorResponse error = new MyErrorResponse("Bad board size. Available board sizes: {15x15}");
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExceptionWinner.class)
    public ResponseEntity<MyErrorResponse> handleWinner(ExceptionBoardLength e) {
        MyErrorResponse error = new MyErrorResponse("Bad winner. Avaiable winners: {0, 1, 2}");
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
