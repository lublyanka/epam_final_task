package com.example.cards.advices;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;

class GlobalExceptionHandlerTest {

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
}
