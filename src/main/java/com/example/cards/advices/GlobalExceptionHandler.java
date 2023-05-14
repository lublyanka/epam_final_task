package com.example.cards.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** The Global Exception Handler */
@ControllerAdvice
@CommonsLog
public class GlobalExceptionHandler {

  /**
   * Handles all Internal Server Error exceptions
   *
   * @param request that resulted in an <code>AccessDeniedException</code>
   * @param ex that caused the invocation
   * @return the view name of the 500 error page
   */
  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleInternalServerError(HttpServletRequest request, Exception ex) {
    if (ex instanceof HttpRequestMethodNotSupportedException)
      log.error(request.getMethod() + " not supported");
    else log.error(ex.getMessage());
    return "/error/500";
  }


  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    String errorMessage = "Invalid argument: " + ex.getMessage();

    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }
}
