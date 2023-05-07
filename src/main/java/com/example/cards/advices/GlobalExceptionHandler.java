package com.example.cards.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(HttpServletRequest request, Exception ex) {
        return "/error/500";
    }

   /* @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedError(HttpServletRequest request, Exception ex) {
        return "/error/403";
    }*/
}
