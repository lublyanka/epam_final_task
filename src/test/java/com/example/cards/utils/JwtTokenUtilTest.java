package com.example.cards.utils;

import com.example.cards.entities.User;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


//TODO
@SpringJUnitConfig
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenUtilTest {

    public static final int TOKEN_VALIDITY_MS = 1000 * 60 * 60 * 1; // 1 hour

    //TODO add to build.gradle test properties
     private UserPrincipal userPrincipal;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(Keys.hmacShaKeyFor(jwtSecret.getBytes()));


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
    }

    @Test
    void generateJwtToken() {

        //Collection<GrantedAuthority> authorities = Arrays.asList(() -> "ROLE_USER", () -> "ROLE_ADMIN");

        Collection<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");

        // Generate JWT token
        String token = jwtTokenUtil.generateJwtToken(userPrincipal);

        // Validate token
        Claims claims = Jwts.parser()
                .setSigningKey(System.getenv("ENV_JWT"))
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userPrincipal.getUsername(), claims.getSubject());
        assertEquals(userPrincipal.getAuthorities(), claims.get("roles"));
      }

    @Test
    void validateToken() {
      }

    @Test
    void isTokenExpired() {
      }

    @Test
    void extractUsername() {
      }

    @Test
    void extractExpiration() {
      }

    @Test
    void extractRoles() {
      }

    @Test
    void extractClaim() {
        String token = "";
        String subject = "1234567890";

        String extractedSubject = jwtTokenUtil.extractClaim(token, Claims::getSubject);

        assertEquals(subject, extractedSubject);
      }

    @Test
    void extractAllClaims() {
      }
}