package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class UserDetailsServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testLoadUserByUsername() {
    String username = "john.doe@gmail.com";
    User user = new User();
    user.setEmail(username);
    userRepository = mock(UserRepository.class);
    when(userRepository.findByEmail(username)).thenReturn(user);

    JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(userRepository);


    UserDetails userPrincipal = jwtUserDetailsService.loadUserByUsername(username);

    assertNotNull(userPrincipal);
    assertEquals(username, userPrincipal.getUsername());
    verify(userRepository).findByEmail(username);
  }

  @Test
  public void testLoadUserByUsername_UserNotFound() {
    String username = "john.doe@gmail.com";
    userRepository = mock(UserRepository.class);
    when(userRepository.findByEmail(username)).thenReturn(null);
    JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(userRepository);
    assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername(username));
    verify(userRepository).findByEmail(username);
  }
}