package com.example.cards.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cards.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenUtilTest {
  public static final int TOKEN_VALIDITY_MS = 1000 * 60 * 60 * 10; // 1 hour
  private static final java.util.logging.Logger log =
      java.util.logging.Logger.getLogger(JwtTokenUtilTest.class.getName());
  private final Key key = Keys.hmacShaKeyFor(System.getenv("ENV_JWT").getBytes());
  private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(key);
  String token;
  private UserPrincipal userPrincipal;

  @BeforeEach
  void createBasicNormalUser() {
    User user = new User();
    user.setName("John");
    user.setSurname("Smith");
    user.setEmail("john@example.com");
    user.setPassword("Qwerty123");
    user.setPhone("1234567890");
    user.setRole("USER");
    userPrincipal = new UserPrincipal(user);
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userPrincipal.getAuthorities());
    token =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(new Date(Instant.now().toEpochMilli()))
            .setExpiration(new Date(Instant.now().toEpochMilli() + TOKEN_VALIDITY_MS))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    log.info(token);
  }

  @Test
  void generateJwtToken() {
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

    assertEquals(userPrincipal.getUsername(), claims.getSubject());
    assertEquals(Map.of("authority", "ROLE_USER"), claims.get("roles", List.class).get(0));
  }

  @Test
  void validateToken() {
    String username = "john@example.com";
    UserPrincipal userDetails = mock(UserPrincipal.class);
    when(userDetails.getUsername()).thenReturn(username);
    boolean result = jwtTokenUtil.validateToken(token, userDetails);
    assertTrue(result);
  }

  @Test
  void isTokenExpired() {
    boolean result = jwtTokenUtil.isTokenExpired(token);
    assertFalse(result);
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userPrincipal.getAuthorities());
    Instant instantNow = Instant.now();
    token =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(new Date(Instant.now().toEpochMilli()))
            .setExpiration(new Date(Instant.now().toEpochMilli() - 2000))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    try {
      result = jwtTokenUtil.isTokenExpired(token);
      assertTrue(result);
    } catch (ExpiredJwtException e) {
      assert (true);
    }
  }

  @Test
  void extractUsername() {
    String username = "john@example.com";
    String result = jwtTokenUtil.extractUsername(token);
    assertEquals(username, result);
  }

  @Test
  void extractExpiration() {
    Date expirationDate = new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MS);
    Date result = jwtTokenUtil.extractExpiration(token);
    assertEquals(expirationDate.toString(), result.toString());
  }

  @Test
  void extractRoles() {
    try (MockedStatic<Jwts> utilities = Mockito.mockStatic(Jwts.class)) {
      List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");
      Claims claims = mock(Claims.class);
      when(claims.get("roles", List.class)).thenReturn(roles);
      JwtParserBuilder builder = mock(JwtParserBuilder.class);
      when(builder.setSigningKey(key)).thenReturn(builder);
      JwtParser parser = mock(JwtParser.class);
      when(builder.build()).thenReturn(parser);
      Jws<io.jsonwebtoken.Claims> jwsString = mock(Jws.class);
      when(parser.parseClaimsJws(token)).thenReturn(jwsString);
      when(jwsString.getBody()).thenReturn(claims);
      utilities.when(() -> Jwts.parserBuilder()).thenReturn(builder);
      List<String> result = jwtTokenUtil.extractRoles(token);
      assertEquals(roles, result);
      verify(builder).setSigningKey(key);
      verify(claims).get("roles", List.class);
    }
  }

  @Test
  void extractClaim() {
    String subject = "john@example.com";
    String extractedSubject = jwtTokenUtil.extractClaim(token, Claims::getSubject);

    assertEquals(subject, extractedSubject);
  }

  @Test
  void extractAllClaims() {
    String token =
        Jwts.builder()
            .setSubject("testuser")
            .claim("email", "testuser@example.com")
            .claim("role", "USER")
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();

    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(key);
    Claims claims = jwtTokenUtil.extractAllClaims(token);

    assertEquals("testuser", claims.getSubject());
    assertEquals("testuser@example.com", claims.get("email"));
    assertEquals("USER", claims.get("role"));
  }
}
