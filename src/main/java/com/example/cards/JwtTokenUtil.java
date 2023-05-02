package com.example.cards;

import com.example.cards.entities.User;
import io.jsonwebtoken.*;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log
public class JwtTokenUtil {

  @Qualifier("jwtKey")
  private final Key jwtSecret;

/*  public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }*/

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", user.getUsername());
    claims.put("roles", user.getAuthorities());
    return createToken(claims);
  }

  private String createToken(Map<String, Object> claims) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 hours

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiration)
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
    return extractClaim(token, x -> {
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
