package com.example.cards.filters;

import com.example.cards.JwtTokenUtil;
import com.example.cards.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Log
public class JwtTokenFilter extends OncePerRequestFilter {

  @Qualifier("jwtKey")
  private final Key jwtSecret;

  private final UserService userDetailsService;
  @Qualifier("securityContextRepository")
  private final SecurityContextRepository  securityContextRepository;
  // private final SecurityContextRepository securityContextRepository;
  // private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = extractJwtFromRequest(request);

      if (StringUtils.hasText(jwt) && !jwtTokenUtil.isTokenExpired(jwt)) {
        Authentication authentication = getAuthentication(jwt);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        //authenticationManager.authenticate(authentication);
        // securityContextRepository.saveContext(context,  request, response);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Set authentication athorities to:" + context.getAuthentication().getAuthorities().toString());
      }
    } catch (SignatureException e) {
      // handle invalid signature
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid JWT signature");
      return;
    } catch (ExpiredJwtException e) {
      // handle expired token
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("JWT token has expired");
      return;
    } catch (Exception e) {
      // handle other errors
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Unexpected error: " + e.getMessage());
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
   /* if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;*/
    return bearerToken;
  }

  private Authentication getAuthentication(String jwt) {
    String userId = jwtTokenUtil.extractUsername(jwt);
    List<String> roles = jwtTokenUtil.extractRoles(jwt);
    log.info("Roles extracted from token: " + roles);

    UserDetails userDetails = userDetailsService.getUserByEmail(userId);

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
