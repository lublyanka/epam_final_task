package com.example.cards.utils;

import com.example.cards.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class UserPrincipalTest {
    private User user;
    @BeforeEach
    void createBasicNormalUser() {
        user = new User();
        user.setName("John");
        user.setSurname("Smith");
        user.setEmail("john@example.com");
        user.setPassword("Qwerty123");
        user.setPhone("1234567890");
        user.setRole("USER");
    }

    @Test
    void getAuthorities() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.getAuthorities().toString().contains("ROLE_USER"));
        user.setRole("admin");
        assertTrue(userPrincipal.getAuthorities().toString().contains("ROLE_ADMIN"));
      }

    @Test
    void getPassword() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(user.getPassword(), userPrincipal.getPassword());
      }

    @Test
    void getUsername() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(user.getEmail(), userPrincipal.getUsername());
      }

    @Test
    void isAccountNonExpired() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isAccountNonExpired());
        user.setBlocked(true);
        assertTrue(userPrincipal.isAccountNonExpired());
      }

    @Test
    void isAccountNonLocked() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isAccountNonLocked());
        user.setBlocked(true);
        assertFalse(userPrincipal.isAccountNonLocked());
      }

    @Test
    void isCredentialsNonExpired() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isCredentialsNonExpired());
        user.setBlocked(true);
        assertTrue(userPrincipal.isCredentialsNonExpired());
      }

    @Test
    void isEnabled() {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isEnabled());
        user.setBlocked(true);
        assertFalse(userPrincipal.isEnabled());
      }
}