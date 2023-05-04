package com.example.cards.userTests;

import com.example.cards.entities.User;
import com.example.cards.enums.Responses;
import com.example.cards.repositories.UserRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import com.example.cards.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertEquals;

// @SpringBootTest
//@ActiveProfiles("test")
public class UserValidationTests {


    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // @Autowired
    private final UserService  userService = new UserService(passwordEncoder);

    // @MockBean private UserRepository userRepository;

    @Test
    public void testEmptyEmail() {
        User user = new User();
        user.setEmail("");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());

        assertEquals("",Responses.EMAIL_IS_EMPTY, error.get());
    }

    @Test
    public void testEmptyPassword() {
        User user = new User();
        user.setPassword("");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.PASSWORD_IS_EMPTY, error.get());
    }

    @Test
    public void testEmptyNameAndSurname() {
        User user = new User();
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
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void testInvalidName(String name) {
        User user = new User();
        user.setSurname("Cloud");
        user.setEmail("123@example.com");
        user.setPassword("Qwerty123");
        user.setName(name);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.NAME_IS_INVALID, error.get());
    }

    @ParameterizedTest
    @MethodSource("validNames")
    public void testValidName(String name) {
        User user = new User();
        user.setName(name);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void testInvalidSurname(String surname) {
        User user = new User();
        user.setSurname(surname);
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.SURNAME_IS_INVALID, error.get());
    }

    @ParameterizedTest
    @MethodSource("phones")
    public void testPhone(String phone, Optional<?> expected) {
        User user = new User();
        user.setPhone(phone);
        Optional<?> error = userService.getValidationUserError(user);
        assertEquals("", expected, error);
    }

    @Test
    public void testInvalidPassword() {
        User user = new User();
        user.setPassword("password");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.PASSWORD_IS_INVALID, error.get());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        Optional<?> error = userService.getValidationUserError(user);
        assertTrue(error.isPresent());
        assertEquals("", Responses.EMAIL_IS_INVALID, error.get());
    }

    @Test
    public void testValidUser() {
        User user = new User();
        user.setName("John");
        user.setSurname("Smith");
        user.setEmail("john@example.com");
        user.setPassword("Password1");
        user.setPhone("1234567890");
        Optional<?> error = userService.getValidationUserError(user);
        assertFalse(error.isPresent());
    }

    static Stream<String> invalidNames() {
        return Stream.of("123", "John Smith1", "J0hn");
    }

    static Stream<String> validNames() {
        return Stream.of("Juan","Andrés", "Jorge","Juan Carlos", "Juan-Manuel","JСветлана");
    }

    private static Collection<Object[]> phones() {
        return Arrays.asList(
                new Object[][] {
                        {"45391488034364674567", Responses.PHONE_IS_INVALID},
                        {"4539 1488", Responses.PHONE_IS_INVALID},
                        {"45391", Optional.empty()},
                        {"45391488", Optional.empty()},
                        {"", Responses.PHONE_IS_INVALID},
                        {"  6011-1111-1111-1117  ", Responses.PHONE_IS_INVALID},
                        {"1", false},
                        {"ABCD EFGH IJKL MNOP", Responses.PHONE_IS_INVALID}
                });
    }

//    @TestConfiguration
//    class UserServiceTestContextConfiguration {
//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder(10);
//        }
//        @Bean
//        public UserService userService() {
//      return new UserService(this.passwordEncoder());
//        }
//    }
}