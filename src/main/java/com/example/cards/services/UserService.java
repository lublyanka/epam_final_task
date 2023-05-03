package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  @Autowired private UserRepository userRepository;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Qualifier("passwordEncoder")
  private final PasswordEncoder passwordEncoder ;

  public Page<User> getAllUsers(
          String sortBy, String sortOrder, int page, int size) {
    return userRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)));
  }

  public Optional<User> getUserById(Long userId) {
    return userRepository.findById(userId);
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User getUserByToken(String token) {
    return userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
  }

  public User getUserByTokenWithCountry(String token) {
    return userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
  }

  public User registerUser(User user) {
    if (isExistsByEmail(user)) {
      return null;
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");
    user = userRepository.save(user);
    userRepository.flush();
    return user;
  }

  public User updateUser(User updatedUser, User userToSave) {
    if (!userToSave.getName().equals(updatedUser.getName()))
      userToSave.setName(updatedUser.getName());

    if (!userToSave.getSurname().equals(updatedUser.getSurname()))
      userToSave.setSurname(updatedUser.getSurname());

    if (!userToSave.getEmail().equals(updatedUser.getEmail()))
      userToSave.setEmail(updatedUser.getEmail());

    if (!userToSave.getPhone().equals(updatedUser.getPhone()))
      userToSave.setPhone(updatedUser.getPhone());

    if (!userToSave.getAddress().equals(updatedUser.getAddress()))
      userToSave.setAddress(updatedUser.getAddress());

    userToSave.setUpdatedOn(Timestamp.from(Instant.now()));

    userToSave = userRepository.save(userToSave);
    userRepository.flush();
    return userToSave;
  }

  public Optional<User> block(Long userId) {
    Optional<User> userOptional = getUserById(userId);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (!user.isBlocked()) {
        user.setBlocked(true);
        user.setUpdatedOn(Timestamp.from(Instant.now()));
        userRepository.save(user);
        userRepository.flush();
      }
      return userOptional;
    } else {
      return Optional.empty();
    }
  }

  public Optional<User> unblock(Long userId) {
    Optional<User> userOptional = getUserById(userId);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (user.isBlocked()) {
        user.setBlocked(false);
        user.setUpdatedOn(Timestamp.from(Instant.now()));
        userRepository.save(user);
        userRepository.flush();
      }
      return userOptional;
    } else {
      return Optional.empty();
    }
  }

  public boolean isExistsByEmail(User user) {
    return userRepository.existsByEmail(user.getEmail());
  }

  public void updateUserLastLogin(User user) {
    user.setUpdatedOn(Timestamp.from(Instant.now()));
    user.setLastLogin(Timestamp.from(Instant.now()));
    userRepository.save(user);
    userRepository.flush();
  }
}
