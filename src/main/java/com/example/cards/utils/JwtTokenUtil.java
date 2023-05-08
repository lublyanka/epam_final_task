package com.example.cards.utils;

import io.jsonwebtoken.*;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/** The Jwt token util. */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  /** The constant TOKEN_VALIDITY_MS. */
  public static final int TOKEN_VALIDITY_MS = 1000 * 60 * 60 * 10; // 10 hours

  @Qualifier("jwtKey")
  private final Key jwtSecret;

  /**
   * Generate jwt token string.
   *
   * @param userDetails the user details
   * @return the string
   */
  public String generateJwtToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userDetails.getAuthorities());
    Instant instantNow = Instant.now();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(instantNow.toEpochMilli()))
        .setExpiration(new Date(instantNow.toEpochMilli() + TOKEN_VALIDITY_MS))
        .signWith(jwtSecret, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Validate token.
   *
   * @param token the token
   * @param userDetails the user details
   * @return the boolean
   */
  public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Is token expired.
   *
   * @param token the token
   * @return the boolean
   */
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extract username string.
   *
   * @param token the token
   * @return the string
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extract expiration date.
   *
   * @param token the token
   * @return the date
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extract roles list.
   *
   * @param token the token
   * @return the list
   */
  public List<String> extractRoles(String token) {
    return extractClaim(
        token,
        x -> {
          List<?> roles = x.get("roles", List.class);
          return roles.stream()
              .filter(role -> role instanceof String)
              .map(role -> (String) role)
              .collect(Collectors.toList());
        });
  }

  /**
   * Extract claim.
   *
   * @param <T> the type parameter
   * @param token the token
   * @param claimsResolver the claims resolver
   * @return the t
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extract all claims from token.
   *
   * @param token the token
   * @return the claims
   */
  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
  }
}
