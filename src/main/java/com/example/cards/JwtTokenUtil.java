package com.example.cards;

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

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  public static final int TOKEN_VALIDITY_MS = 1000 * 60 * 60 * 10; // 10 hours

  @Qualifier("jwtKey")
  private final Key jwtSecret;

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

  public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

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

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
  }
}
