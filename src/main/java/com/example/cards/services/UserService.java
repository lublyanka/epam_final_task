package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  @Autowired private JwtTokenUtil jwtTokenUtil;

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
    return userRepository.findByEmailWithCountry(jwtTokenUtil.extractUsername(token));
  }

  public User registerUser(User user) {
    if (isExistsByEmail(user)) {
      return null;
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    user.setPassword(encoder.encode(user.getPassword()));
    user.setRole("user");
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

    if (!userToSave.getUserAddress().equals(updatedUser.getUserAddress()))
      userToSave.setUserAddress(updatedUser.getUserAddress());

    userToSave.setUpdatedOn(Timestamp.from(Instant.now()));

    userToSave = userRepository.save(userToSave);
    userRepository.flush();
    return userToSave;
  }

  public Optional<User> block(Long userId) {
    Optional<User> userOptional = getUserById(userId);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (user.isEnabled()) {
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
      if (!user.isEnabled()) {
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
