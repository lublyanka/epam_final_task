package com.example.cards.requests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoginRequestTest {
@Test
public void testGetEmail() {
    // Prepare test data
    String email = "test@example.com";
    String password = "password";

    // Create a new LoginRequest instance with the test data
    LoginRequest loginRequest = new LoginRequest(email, password);

    // Call the getEmail method
    String retrievedEmail = loginRequest.getEmail();

    // Assert the result
    assertEquals(email, retrievedEmail);
}

    @Test
    public void testGetPassword() {
        // Prepare test data
        String email = "test@example.com";
        String password = "password";

        // Create a new LoginRequest instance with the test data
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call the getPassword method
        String retrievedPassword = loginRequest.getPassword();

        // Assert the result
        assertEquals(password, retrievedPassword);
    }
}

