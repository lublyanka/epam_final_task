package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.example.cards.entities.User;
import com.example.cards.enums.Responses;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserValidationTests {

    private final UserService userService = new UserService(null, null, null);



    private User user;

    static Stream<String> invalidNames() {
        return Stream.of("123", "John Smith1", "J0hn");
    }

    static Stream<String> validNames() {
        return Stream.of("Andrés", "Juan Carlos", "Juan-Manuel","Moño", "JСветлана", "Борис");
    }

    private static Collection<Object[]> phones() {
        return Arrays.asList(
                new Object[][] {
                        {"453914880343646745671", Optional.of(Responses.PHONE_IS_INVALID)},
                        {"4539 1488", Optional.of(Responses.PHONE_IS_INVALID)},
                        {"45391", Optional.empty()},
                        {"45391488", Optional.empty()},
                        {"", Optional.of(Responses.PHONE_IS_INVALID)},
                        {"  6011-1111-1111-1117  ", Optional.of(Responses.PHONE_IS_INVALID)},
                        {"1", Optional.of(Responses.PHONE_IS_INVALID)},
                        {"ABCD EFGH IJKL MNOP", Optional.of(Responses.PHONE_IS_INVALID)}
                });
    }

    @BeforeEach
    void createBasicNormalUser() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Smith");
        user.setMiddlename("Dow");
        user.setEmail("john@example.com");
        user.setPassword("Qwerty123");
        user.setPhone("1234567890");
        user.setRole("USER");
    }

    @Test
    public void testEmptyEmail() {
        user.setEmail("");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());

        assertEquals("",Responses.EMAIL_IS_EMPTY, error.get());
    }

    @Test
    public void testEmptyPassword() {
        user.setPassword("");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.PASSWORD_IS_EMPTY, error.get());
    }

    @Test
    public void testEmptyNameAndSurname() {
        user.setName("");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_EMPTY, error.get());
        user.setSurname("");
        error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_EMPTY, error.get());
        user.setName(null);
        error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_EMPTY, error.get());
        user.setName("Dow");
        error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_EMPTY, error.get());
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void testInvalidName(String name) {
        user.setName(name);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_INVALID, error.get());
    }

    @ParameterizedTest
    @MethodSource("validNames")
    public void testValidName(String name) {
        user.setName(name);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void testInvalidSurname(String surname) {
        user.setSurname(surname);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.SURNAME_IS_INVALID, error.get());
    }

    @ParameterizedTest
    @MethodSource("phones")
    public void testPhone(String phone, Optional<?> expected) {
        user.setPhone(phone);
        Optional<?> error = userService.getValidationUserError(user);
        assertEquals("", expected, error);
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "pass123", "@", "Password", "pass word"})
    public void testInvalidPassword(String password) {
        user.setPassword(password);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.PASSWORD_IS_INVALID, error.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "invalid.email", "invalid.email@", "invalid.email.com.", "invalid email@example.com"})
    public void testInvalidEmail(String email) {
        user.setEmail(email);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.EMAIL_IS_INVALID, error.get());
    }

    @Test
    public void testValidUser() {
        Optional<?> error = userService.getValidationUserError(user);
        assertFalse(error.isPresent());
    }
    @Test
    void testIsAdmin() {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        User regularUser = new User();
        regularUser.setRole("USER");
        assertTrue(adminUser.isAdmin());
        assertFalse(regularUser.isAdmin());
    }

    @Test
    void testUserEquals() {
        User user1 = user;
        User user2 = new User();
        user2.setId(1L);
        assertTrue(user1.equals(user2));
        user2.setId(2L);
        assertFalse(user1.equals(user2));
        user1.setId(null);
        assertFalse(user1.equals(user2));
        user2.setId(null);
        assertTrue(user1.equals(user2));
    }


    @Test
    void testUserFields() {
        user.setAddress("Calle 1");
        Assertions.assertEquals("Calle 1", user.getAddress());
        Assertions.assertEquals("USER", user.getRole());
        Timestamp now = Timestamp.from(Instant.now());
        user.setLastLogin(now);
        user.setBirthDate(now);
        user.setCreatedOn(now);
        Assertions.assertEquals(now, user.getLastLogin());
        Assertions.assertEquals(now, user.getBirthDate());
        Assertions.assertEquals(now, user.getCreatedOn());
        Assertions.assertEquals("Dow", user.getMiddlename());
        user.setMiddlename("Bow");
        Assertions.assertNotEquals("Dow", user.getMiddlename());
        Assertions.assertEquals("Bow", user.getMiddlename());
    }
}