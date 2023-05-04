package com.example.cards.services;

import static com.example.cards.enums.Responses.*;

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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  @Qualifier("passwordEncoder")
  private final PasswordEncoder passwordEncoder;

  @Autowired private UserRepository userRepository;
  @Autowired private JwtTokenUtil jwtTokenUtil;

  public Page<User> getAllUsers(String sortBy, String sortOrder, int page, int size) {
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

  public Optional<?> getValidationUserError(User user) {
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      return Optional.of(EMAIL_IS_EMPTY);
    }
    if (user.getPassword() == null || user.getPassword().isEmpty()) {
      return Optional.of(PASSWORD_IS_EMPTY);
    }
    if (user.getSurname() == null
        || user.getSurname().isEmpty()
        || user.getName() == null
        || user.getName().isEmpty()) {
      return Optional.of(NAME_IS_EMPTY);
    }

    if (isTextStringInvalid(user.getName())) {
      return Optional.of(NAME_IS_INVALID);
    }

    if (isTextStringInvalid(user.getSurname())) {
      return Optional.of(SURNAME_IS_INVALID);
    }

    //Allow only digits, from 5 to 20 characters length
    if (!user.getPhone().matches("^\\d{5,20}$")) {
      return Optional.of(PHONE_IS_INVALID);
    }

    // Allow password not less than 8 character with  at least one digit, one uppercase letter, only in Latin
    if (!user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")) {
      return Optional.of(PASSWORD_IS_INVALID);
    }

    if (!user.getEmail().matches("^[\\w-+.]+@([\\w-]+.)+[\\w-]{2,4}$")) {
      return Optional.of(EMAIL_IS_INVALID);
    }

    // TODO add check Data: more than 14 years old

    // TODO add check Data format

    return Optional.empty();
  }

  private boolean isTextStringInvalid(String str) {
    // Allow letters, spaces, apostrophes, and hyphens, as well as Cyrillic and Spanish characters
    return !str.matches(
        "[a-zA-Z\\u00C0-\\u024F\\u0400-\\u04FF\\u0500-\\u052F\\u1E00-\\u1EFF\\s'’\\-]+");
  }

  @Transactional
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

  @Transactional
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

  @Transactional
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

  @Transactional
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

  @Transactional
  public void updateUserLastLogin(User user) {
    user.setUpdatedOn(Timestamp.from(Instant.now()));
    user.setLastLogin(Timestamp.from(Instant.now()));
    userRepository.save(user);
    userRepository.flush();
  }
}
