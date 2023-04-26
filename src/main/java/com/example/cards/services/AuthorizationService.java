package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
  @Autowired UserService userService;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  public String checkUserPassword(String password, String email) {
    User user = userService.getUserByEmail(email);
    if (user == null) {
      return null;
    }

    if (!isPasswordMatch(password, user)) {
      return null;
    }

    return getToken(user);
  }

  private boolean isPasswordMatch(String password, User user) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    return encoder.matches(password, user.getPassword());
  }

  private String getToken(User user) {
    String token = jwtTokenUtil.generateToken(user);
    userService.updateUserLastLogin(user);
    return token;
  }
}
