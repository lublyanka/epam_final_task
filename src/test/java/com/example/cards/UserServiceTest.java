/*
package com.example.cards;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import com.example.cards.services.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UserServiceTest {

    @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder;

    @TestConfiguration
    class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserService(passwordEncoder);
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
    User alex = new User();
    alex.setEmail("alex777@gmail.com");
    alex.setPassword("123456");
    alex.setSurname("Dolon");
    alex.setName("Alex");
    alex.setPhone("0987654321");

    Mockito.when(userRepository.findByEmail("alex777@gmail.com")).thenReturn(alex);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "alex777@gmail.com";
        User found = userService.getUserByEmail(email);

        assertEquals("failure - strings are not equal", found.getEmail(),email);
    }
}
*/
