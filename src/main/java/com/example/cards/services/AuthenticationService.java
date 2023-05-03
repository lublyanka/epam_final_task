package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.MyUserPrincipal;
import com.example.cards.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  @Autowired UserService userService;

  @Qualifier("passwordEncoder")
  private final PasswordEncoder passwordEncoder ;

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
    return passwordEncoder.matches(password, user.getPassword());
  }

  private String getToken(User user) {
    //String token = jwtTokenUtil.generateToken(user);
    String token = jwtTokenUtil.generateJwtToken(new MyUserPrincipal(user));
    userService.updateUserLastLogin(user);
    return token;
  }
}
