package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.UserPrincipal;
import com.example.cards.entities.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  @Qualifier("passwordEncoder")
  private final PasswordEncoder passwordEncoder;

  @Autowired UserService userService;
  @Autowired private JwtTokenUtil jwtTokenUtil;

  public Optional<?> isUserValid(String password, String email) {
    User user = userService.getUserByEmail(email);
    if (user == null) {
      return Optional.empty();
    }

    if (!isPasswordValid(password, user)) return Optional.empty();

    if (user.isBlocked()) return Optional.of(true);

    return Optional.ofNullable(getToken(user));
  }

  private boolean isPasswordValid(String password, User user) {
    return isPasswordMatch(password, user.getPassword());
  }

  private boolean isPasswordMatch(String password, String userPassword) {
    return passwordEncoder.matches(password, userPassword);
  }

  private String getToken(User user) {
    String token = jwtTokenUtil.generateJwtToken(new UserPrincipal(user));
    userService.updateUserLastLogin(user);
    return token;
  }

  public boolean isRequestEmpty(String email, String password) {
    return email == null || password == null || email.isEmpty() || password.isEmpty();
  }
}
