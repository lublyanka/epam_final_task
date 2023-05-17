package com.example.cards.controllers.auth;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private  UserProfileController  userProfileController;
    User user;
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
    public void testLoadUserProfile() {
        String token = "your-token";
        User user = new User();
        when(userService.getUserByToken(token)).thenReturn(user);

        ResponseEntity<?> response = userProfileController.loadUserProfile(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        User userToUpdate = user;
        userToUpdate.setEmail("newemail@example.com");
        userToUpdate.setRole("USER");

        User userToSave = new User();
        userToSave.setId(1L);
        userToSave.setEmail("oldemail@example.com");

        String token = "your-token";
        when(userService.getUserByToken(token)).thenReturn(userToUpdate);

        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.of(userToSave));
        when(userService.getUserByEmail(userToUpdate.getEmail())).thenReturn(null);
        when(userService.updateUser(userToUpdate, userToSave)).thenReturn(userToSave);
        when(userService.isValid(userToUpdate)).thenReturn(true);

        ResponseEntity<?> response = userProfileController.updateUser(token,userToUpdate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userToSave, response.getBody());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User userToUpdate = user;
        userToUpdate.setEmail("newemail@example.com");
        userToUpdate.setRole("USER");

        String token = "your-token";
        when(userService.getUserByToken(token)).thenReturn(userToUpdate);

        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.empty());
        when(userService.isValid(userToUpdate)).thenReturn(true);

        ResponseEntity<?> response = userProfileController.updateUser(token,userToUpdate);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        User userToUpdate = user;
        userToUpdate.setEmail("existingemail@example.com");
        userToUpdate.setRole("USER");

        User userToSave = new User();
        userToSave.setId(2L);
        userToSave.setEmail("existingemail@example.com");
        String token = "your-token";
        when(userService.getUserByToken(token)).thenReturn(userToUpdate);
        when(userService.isValid(userToUpdate)).thenReturn(true);
        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        when(userService.getUserByEmail(userToUpdate.getEmail())).thenReturn(userToSave);

        ResponseEntity<?> response = userProfileController.updateUser(token,userToUpdate);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserIsNotValid() {
        User userToUpdate = user;
        userToUpdate.setEmail("existingemail@example.com");
        userToUpdate.setRole("USER");

        User userToSave = new User();
        userToSave.setId(2L);
        userToSave.setEmail("existingemail@example.com");
        String token = "your-token";
        when(userService.isValid(userToUpdate)).thenReturn(false);

        ResponseEntity<?> response = userProfileController.updateUser(token,userToUpdate);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateUser_UnauthorizedUpdate() {

        String token = "invalid_token";
        User userToUpdate = user;
        User userCurrent = new User();
        userCurrent.setId(2L);
        userCurrent.setRole("USER");
        when(userService.getUserByToken(token)).thenReturn(userCurrent);
        when(userService.isValid(userToUpdate)).thenReturn(true);
        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));

        ResponseEntity<?> response = userProfileController.updateUser(token, userToUpdate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You can't update other users", response.getBody());
    }

}
