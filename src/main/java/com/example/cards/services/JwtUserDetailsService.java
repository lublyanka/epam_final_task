package com.example.cards.services;

import com.example.cards.utils.UserPrincipal;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** The Jwt user details service. */
@Service
public class JwtUserDetailsService implements UserDetailsService {
  @Autowired private UserRepository userRepository;

  /**
   * Load user by username.
   *
   * @param username the username identifying the user whose data is required.
   * @return the user details
   * @throws UsernameNotFoundException the username not found exception
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new UserPrincipal(user);
  }
}
