package com.example.cards.services;

import com.example.cards.utils.JwtTokenUtil;
import com.example.cards.utils.UserPrincipal;
import com.example.cards.entities.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The Authentication service. */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  @Qualifier("passwordEncoder")
  private final PasswordEncoder passwordEncoder;

  private final UserService userService;

  private final JwtTokenUtil jwtTokenUtil;

  /**
   * Is user valid.
   *
   * @param password the password
   * @param email the email
   * @return the optional
   */
  @Transactional
  public Optional<?> isUserValid(String password, String email) {
    User user = userService.getUserByEmail(email);
    if (user == null) {
      return Optional.empty();
    }

    if (!isPasswordValid(password, user))
      return Optional.empty();

    if (user.isBlocked())
      return Optional.of(true);

    return Optional.ofNullable(getToken(user));
  }

  /**
   * Is password valid for this user.
   *
   * @param password the password
   * @param user the User
   * @return the optional
   */
  private boolean isPasswordValid(String password, User user) {
    return passwordEncoder.matches(password, user.getPassword());
  }

  /**
   * Gets JWT token.
   *
   * @param user User
   * @return String token
   */
  private String getToken(User user) {
    String token = jwtTokenUtil.generateJwtToken(new UserPrincipal(user));
    userService.updateUserLastLogin(user);
    return token;
  }

  /**
   * Is request empty.
   *
   * @param email the email
   * @param password the password
   * @return the boolean
   */
  public boolean isRequestEmpty(String email, String password) {
    return email == null || password == null || email.isEmpty() || password.isEmpty();
  }
}
