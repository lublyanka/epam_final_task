package com.example.cards.controllers.auth;

import com.example.cards.entities.User;
import com.example.cards.services.AccountService;
import com.example.cards.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private  UserProfileController  userProfileController;
    @Test
    public void testLoadUserProfile() {
        String token = "your-token";
        User user = new User();
        when(userService.getUserByToken(token)).thenReturn(user);

        UserProfileController userProfileController = new UserProfileController();

        ReflectionTestUtils.setField(userProfileController, "userService", userService);

        ResponseEntity<?> response = userProfileController.loadUserProfile(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setEmail("newemail@example.com");

        User userToSave = new User();
        userToSave.setId(1L);
        userToSave.setEmail("oldemail@example.com");

        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.of(userToSave));
        when(userService.getUserByEmail(userToUpdate.getEmail())).thenReturn(null);
        when(userService.updateUser(userToUpdate, userToSave)).thenReturn(userToSave);
        UserProfileController userProfileController = new UserProfileController();

        ReflectionTestUtils.setField(userProfileController, "userService", userService);

        ResponseEntity<?> response = userProfileController.updateUser(userToUpdate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userToSave, response.getBody());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setEmail("newemail@example.com");

        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.empty());

        UserProfileController userProfileController = new UserProfileController();

        ReflectionTestUtils.setField(userProfileController, "userService", userService);

        ResponseEntity<?> response = userProfileController.updateUser(userToUpdate);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setEmail("existingemail@example.com");

        User userToSave = new User();
        userToSave.setId(1L);
        userToSave.setEmail("existingemail@example.com");

        when(userService.getUserById(userToUpdate.getId())).thenReturn(Optional.of(userToSave));
        when(userService.getUserByEmail(userToUpdate.getEmail())).thenReturn(userToSave);
        UserProfileController userProfileController = new UserProfileController();

        ReflectionTestUtils.setField(userProfileController, "userService", userService);

        ResponseEntity<?> response = userProfileController.updateUser(userToUpdate);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
