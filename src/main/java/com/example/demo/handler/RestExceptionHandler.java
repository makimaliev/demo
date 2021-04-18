package com.example.demo.handler;

import com.example.demo.exception.*;
import com.example.demo.payload.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleException(ResourceNotFoundException ex) throws Exception {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setCode("NOT_FOUND");
        response.setTimestamp(LocalDateTime.now());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleException(BadRequestException ex) throws Exception {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setCode("BAD_REQUEST");
        response.setTimestamp(LocalDateTime.now());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleException(AppException ex) throws Exception {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setCode("INTERNAL_SERVER_ERROR");
        response.setTimestamp(LocalDateTime.now());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleException(UnauthorizedException ex) throws Exception {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setCode("UNAUTHORIZED");
        response.setTimestamp(LocalDateTime.now());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }
}
