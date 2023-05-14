package com.example.cards.advices;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;

class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  public void testHandleInternalServerError() {
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    Exception ex = new Exception("Internal Server Error");

    String viewName = exceptionHandler.handleInternalServerError(request, ex);

    assertEquals("/error/500", viewName);
  }

  @Test
  public void testHandleInternalServerError_MethodNotSupported() {
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("POST");
    Exception ex = new HttpRequestMethodNotSupportedException("POST");

    String viewName = exceptionHandler.handleInternalServerError(request, ex);

    assertEquals("/error/500", viewName);
  }

  @Test
  public void testHandleInternalServerError_LogError() {
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    Exception ex = new Exception("Some error message");

    String viewName = exceptionHandler.handleInternalServerError(request, ex);

    assertEquals("/error/500", viewName);
  }

  @Test
  public void testHandleIllegalArgumentException() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

    ResponseEntity<String> responseEntity = exceptionHandler.handleIllegalArgumentException(exception);

    assertEquals(responseEntity.getStatusCode().value(),HttpStatus.BAD_REQUEST.value());

    assertEquals("Invalid argument: Invalid argument", responseEntity.getBody());
  }
}
