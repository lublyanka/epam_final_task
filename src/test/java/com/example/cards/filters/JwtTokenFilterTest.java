package com.example.cards.filters;

import com.example.cards.entities.User;
import com.example.cards.services.JwtUserDetailsService;
import com.example.cards.utils.JwtTokenUtil;
import com.example.cards.utils.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {
  @Mock private Key jwtSecret;

  @Mock private JwtUserDetailsService jwtUserDetailsService;

  @Mock private SecurityContextRepository securityContextRepository;
  @Mock private JwtTokenUtil jwtTokenUtil;

  @Test
  public void testDoFilterInternal_ValidToken_SetAuthentication()
      throws ServletException, IOException {

    String token = "validToken";
    try (MockedStatic<SecurityContextHolder> utilities =
        Mockito.mockStatic(SecurityContextHolder.class)) {
      // Create the JwtTokenFilter instance
      JwtTokenFilter jwtTokenFilter =
          new JwtTokenFilter(
              jwtSecret, jwtUserDetailsService, securityContextRepository, jwtTokenUtil);

      // Create mock objects for HttpServletRequest, HttpServletResponse, and FilterChain
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      FilterChain filterChain = mock(FilterChain.class);

      // Set up the behavior of the mock HttpServletRequest to return a valid JWT token
      when(request.getHeader("Authorization")).thenReturn(token);

      // Set up the behavior of the mock JwtTokenUtil to indicate that the token is valid and not
      // expired
      when(jwtTokenUtil.isTokenExpired(token)).thenReturn(false);
      when(jwtTokenUtil.extractUsername(token)).thenReturn("john@example.com");
      List<String> roles = List.of("ROLE_USER");
      Claims claims = mock(Claims.class);
      //when(claims.get("roles", List.class)).thenReturn(roles);
      when(jwtTokenUtil.extractRoles(token)).thenReturn(roles);
      UserPrincipal userPrincipal = mock(UserPrincipal.class);
      Collection<? extends GrantedAuthority> authorities = List.of(new GrantedAuthorityMock());
      // when(userPrincipal.getAuthorities()).thenReturn(authorities);
      when(jwtUserDetailsService.loadUserByUsername("john@example.com")).thenReturn(userPrincipal);
      //when(response.getWriter()).thenReturn(new PrintWriter(System.out));
      SecurityContext securityContextMock = mock(SecurityContext.class);
      utilities.when(() -> SecurityContextHolder.createEmptyContext()).thenReturn(securityContextMock);;
      //utilities.when(() -> SecurityContextHolder.setContext(securityContextMock));
      Authentication authentication = mock(Authentication.class);
      when(securityContextMock.getAuthentication()).thenReturn(authentication);
      when(authentication.getAuthorities()).thenReturn(List.of());

      // Invoke the doFilterInternal method
      jwtTokenFilter.doFilterInternal(request, response, filterChain);

      // Verify that the authentication is set in the SecurityContextHolder and saved in the
      // SecurityContextRepository

      utilities.verify(() -> SecurityContextHolder.setContext(securityContextMock));
      verify(securityContextRepository).saveContext(any(), eq(request), eq(response));

      // Verify that the filterChain.doFilter method is called
      verify(filterChain).doFilter(request, response);
    }
  }

  private static class GrantedAuthorityMock implements GrantedAuthority {
    @Override
    public String getAuthority() {
      return "ROLE_USER";
    }
  }
}
