package com.example.cards.advices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class CustomAccessDeniedHandlerTest {

  @Test
  public void testHandle() throws IOException {
    CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

    // Create a mock Authentication object
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    // Set the mock Authentication object in the SecurityContextHolder
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testuser");

    accessDeniedHandler.handle(request, response, accessDeniedException);

    // TODO how to check log

    // Verify that the response was redirected to the correct error page
    assertEquals("/error/403", response.getRedirectedUrl());
  }
}
