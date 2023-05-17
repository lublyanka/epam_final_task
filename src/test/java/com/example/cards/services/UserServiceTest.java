package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import com.example.cards.utils.JwtTokenUtil;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final UserRepository userRepository = mock(UserRepository.class);
  private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
  private final UserService userService =
      new UserService(passwordEncoder, userRepository, jwtTokenUtil);

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setName("John");
    user.setSurname("Smith");
    user.setEmail("john@example.com");
    user.setPassword("Qwerty123");
    user.setPhone("1234567890");
    user.setBirthDate(Timestamp.from(Instant.now()));
  }

  @Test
  void getAllUsers() { // Prepare test data
    String sortBy = "name";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;
    User user2 = new User();
    user2.setId(2L);
    user2.setName("John2");
    user2.setSurname("Smith2");
    user2.setEmail("john2@example.com");
    user2.setPassword("Qwerty1232");
    user2.setPhone("123456");

    User user3 = new User();
    user3.setId(3L);
    user3.setName("John3");
    user3.setSurname("Smith3");
    user3.setEmail("john3@example.com");
    user3.setPassword("Qwerty1234");
    user3.setPhone("0987465");
    List<User> users = List.of(user, user2, user3);
    Page<User> expectedPage = new PageImpl<>(users);

    when(userRepository.findAll(any(PageRequest.class))).thenReturn(expectedPage);

    Page<User> result = userService.getAllUsers(sortBy, sortOrder, page, size);

    // Verify that the userRepository's findAll method is called with the correct arguments
    ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
    verify(userRepository, times(1)).findAll(pageRequestCaptor.capture());
    PageRequest pageRequest = pageRequestCaptor.getValue();
    assertEquals(page, pageRequest.getPageNumber());
    assertEquals(size, pageRequest.getPageSize());
    assertEquals(
        Sort.Direction.fromString(sortOrder),
        Objects.requireNonNull(pageRequest.getSort().getOrderFor(sortBy)).getDirection());

    assertEquals(expectedPage, result);
  }

  @Test
  void getUserById() {

    Optional<User> expectedUser = Optional.of(user);
    when(userRepository.findById(user.getId())).thenReturn(expectedUser);
    Optional<User> result = userService.getUserById(user.getId());
    assertEquals(expectedUser, result);
  }

  @Test
  void getUserByEmail() {
    String email = "john@example.com";
    String otherEmail = "XXXXXXXXXXXXXXXXX";
    when(userRepository.findByEmail(email)).thenReturn(user);
    User result = userService.getUserByEmail(email);
    assertEquals(user, result);
    result = userService.getUserByEmail(otherEmail);
    assertNotEquals(user, result);
  }

  @Test
  void getUserByToken() {
    String token = "sample_token";
    String email = "john@example.com";
    when(jwtTokenUtil.extractUsername(token)).thenReturn(email);
    when(userRepository.findByEmail(email)).thenReturn(user);
    User result = userService.getUserByToken(token);
    assertEquals(user, result);
  }

  @Test
  void registerUser() {
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
    String encodedPassword = "$2a$10$FWZwsbqk/bNYz4tG0/D0s.0zyPbVL1VSomZqLmlVI.cdQxMZqCBce";
    when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
    User savedUser = new User();
    savedUser.setName("John");
    savedUser.setSurname("Smith");
    savedUser.setEmail("john@example.com");
    savedUser.setPassword("Qwerty123");
    savedUser.setPhone("1234567890");
    when(userRepository.save(user)).thenReturn(savedUser);
    User result = userService.registerUser(user);
    assertEquals(savedUser, result);
  }
  @Test
  void registerUserAlreadyExists() {
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
    User result = userService.registerUser(user);
    assertNull(result);
  }

  @Test
  void updateUser_differentAddress() {
    User existingUser = user;
    Instant now = Instant.now();
    existingUser.setUpdatedOn(Timestamp.from(now.minusMillis(10000)));
    existingUser.setAddress("123 Main Street");

    User updatedUser = new User();
    updatedUser.setId(1L);
    updatedUser.setName("John1");
    updatedUser.setSurname("Smith1");
    updatedUser.setEmail("john@example.com1");
    updatedUser.setPassword("Qwerty1231");
    updatedUser.setPhone("12345678901");
    updatedUser.setAddress("123 Main Street1");

    when(userRepository.save(existingUser)).thenReturn(updatedUser);

    User result = userService.updateUser(updatedUser, existingUser);

    assertEquals(updatedUser.getAddress(), result.getAddress());
    verify(userRepository, times(1)).save(result);
    verify(userRepository, times(1)).flush();
  }

  @Test
  void updateUser() {
    User existingUser = user;
    Instant now = Instant.now();
    existingUser.setUpdatedOn(Timestamp.from(now.minusMillis(10000)));

    // Create an updated user with modified attributes
    User updatedUser = new User();
    updatedUser.setId(1L);
    updatedUser.setName("John1");
    updatedUser.setSurname("Smith1");
    updatedUser.setEmail("john@example.com1");
    updatedUser.setPassword("Qwerty1231");
    updatedUser.setPhone("12345678901");
    updatedUser.setAddress("123 Main Street1");

    when(userRepository.save(existingUser)).thenReturn(updatedUser);

    User result = userService.updateUser(updatedUser, existingUser);

    assertEquals(updatedUser.getId(), result.getId());
    assertEquals(updatedUser.getName(), result.getName());
    assertEquals(updatedUser.getSurname(), result.getSurname());
    assertEquals(updatedUser.getEmail(), result.getEmail());
    assertEquals(updatedUser.getPhone(), result.getPhone());
    assertEquals(updatedUser.getAddress(), result.getAddress());
    assertNotEquals(existingUser.getUpdatedOn(), result.getUpdatedOn());
    verify(userRepository, times(1)).save(result);
    verify(userRepository, times(1)).flush();
  }

  @Test
  void testBlock_UserExistsAndIsNotBlocked() {
    Long userId = 1L;
    user.setBlocked(false);
    Instant now = Instant.now();
    user.setUpdatedOn(Timestamp.from(now));

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.block(userId);

    assertTrue(result.isPresent());
    assertTrue(result.get().isBlocked());
    assertEquals(user.getUpdatedOn(), result.get().getUpdatedOn());
    verify(userRepository, times(1)).save(result.get());
    verify(userRepository, times(1)).flush();
  }

  @Test
  void testBlock_UserExistsAndIsBlocked() {
    Long userId = 1L;
    user.setBlocked(true);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.block(userId);

    assertTrue(result.isPresent());
    assertTrue(result.get().isBlocked());
    assertEquals(user.getUpdatedOn(), result.get().getUpdatedOn());
    verify(userRepository, never()).save(any());
    verify(userRepository, never()).flush();
  }

  @Test
  void testBlock_UserDoesNotExist() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Optional<User> result = userService.block(userId);

    assertFalse(result.isPresent());
    verify(userRepository, never()).save(any());
    verify(userRepository, never()).flush();
  }

  @Test
  void testUnblock_UserExistsAndIsBlocked() {
    Long userId = 1L;
    user.setBlocked(true);
    Instant now = Instant.now();
    user.setUpdatedOn(Timestamp.from(now));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.unblock(userId);

    assertTrue(result.isPresent());
    assertFalse(result.get().isBlocked());
    assertEquals(user.getUpdatedOn(), result.get().getUpdatedOn());
    verify(userRepository, times(1)).save(result.get());
    verify(userRepository, times(1)).flush();
  }

  @Test
  void testUnblock_UserExistsAndIsNotBlocked() {
    Long userId = 1L;
    user.setBlocked(false);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.unblock(userId);

    assertTrue(result.isPresent());
    assertFalse(result.get().isBlocked());
    assertEquals(user.getUpdatedOn(), result.get().getUpdatedOn());
    verify(userRepository, never()).save(any());
    verify(userRepository, never()).flush();
  }

  @Test
  void testUnblock_UserDoesNotExist() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Optional<User> result = userService.unblock(userId);

    assertFalse(result.isPresent());
    verify(userRepository, never()).save(any());
    verify(userRepository, never()).flush();
  }

  @Test
  void isExistsByEmail() {
    when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
    when(userRepository.existsByEmail("john@example1.com")).thenReturn(false);
    boolean result = userService.isExistsByEmail(user);
    assertTrue(result);
    User user1 = new User();
    user1.setEmail("john@example1.com");
    result = userService.isExistsByEmail(user1);
    assertFalse(result);
  }

  @Test
  void updateUserLastLogin() {
    userService.updateUserLastLogin(user);
    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).flush();
    assertNotNull(user.getLastLogin());
    assertNotNull(user.getUpdatedOn());
  }

  @Test
  public void testIsValid_AllFieldsNonNull_ReturnsTrue() {
    boolean isValid = userService.isValid(user);
    assertTrue(isValid);
  }

  @Test
  public void testIsValid_AnyFieldNull_ReturnsFalse() {
    user.setName(null);
    boolean isValid = userService.isValid(user);
    assertFalse(isValid);
  }
}
