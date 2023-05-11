package com.example.cards.utils;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.security.Key;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenUtilTest {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(JwtTokenUtilTest.class.getName());
    public static final int TOKEN_VALIDITY_MS = 1000 * 60 * 60*10; // 1 hour
     private final Key key = Keys.hmacShaKeyFor(System.getenv("ENV_JWT").getBytes());
    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(key);

    private UserPrincipal userPrincipal;
    String token ;


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
        token = jwtTokenUtil.generateJwtToken(userPrincipal);
        log.info(token);
    }

    @Test
    void generateJwtToken() {

        //Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userPrincipal.getAuthorities();
        Collection<GrantedAuthority> authorities = List.of(() -> "authority=ROLE_USER");


        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userPrincipal.getUsername(), claims.getSubject());
        assertEquals(authorities, claims.get("roles"));
      }

    @Test
    void validateToken() {
      }

    @Test
    void isTokenExpired() {
        Boolean result = jwtTokenUtil.isTokenExpired(token);
        assertFalse(result);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userPrincipal.getAuthorities());
        Instant instantNow = Instant.now();
        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().toEpochMilli() -2000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        try{result = jwtTokenUtil.isTokenExpired(token);
        assertTrue(result);}
        catch (ExpiredJwtException e){
            assert(true);
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
      }

    @Test
    void extractClaim() {
        String subject = "john@example.com";
        String extractedSubject = jwtTokenUtil.extractClaim(token, Claims::getSubject);

        assertEquals(subject, extractedSubject);
      }

    @Test
    void extractAllClaims() {
      }
}