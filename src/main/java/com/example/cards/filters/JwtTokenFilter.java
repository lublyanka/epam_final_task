package com.example.cards.filters;

import com.example.cards.services.JwtUserDetailsService;
import com.example.cards.utils.JwtTokenUtil;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/** The Jwt token filter. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Log
public class JwtTokenFilter extends OncePerRequestFilter {

  @Qualifier("jwtKey")
  private final Key jwtSecret;

  private final JwtUserDetailsService jwtUserDetailsService;

  @Qualifier("securityContextRepository")
  private final SecurityContextRepository securityContextRepository;

  private final JwtTokenUtil jwtTokenUtil;

  /**
   * Filter method for JWT token, checks its validity
   *
   * @param request HTTP servlet request
   * @param response HTTP serblet response
   * @param filterChain Spring Servlet chain of filters
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = extractJwtFromRequest(request);

      if (StringUtils.hasText(jwt) && !jwtTokenUtil.isTokenExpired(jwt)) {
        Authentication authentication = getAuthentication(jwt);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        log.info(
            "Set authentication athorities to:"
                + context.getAuthentication().getAuthorities().toString());
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

  /**
   * Extract JWT token from request
   *
   * @param request HTTP servlet request
   * @return JWT token
   */
  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    /* if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;*/
    return bearerToken;
  }

  /**
   * Get Authentication object from JWT token
   *
   * @param jwt JWT token in String format
   * @return new Authentication object
   */
  private Authentication getAuthentication(String jwt) {
    String userEmail = jwtTokenUtil.extractUsername(jwt);
    List<String> roles = jwtTokenUtil.extractRoles(jwt);
    log.info("Roles extracted from token: " + roles);

    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userEmail);

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
