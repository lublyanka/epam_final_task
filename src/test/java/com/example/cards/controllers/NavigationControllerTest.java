package com.example.cards.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NavigationControllerTest {
    private final NavigationController navigationController = new NavigationController();

    @Test
    public void testGetIndex() {
        String result = navigationController.getIndex();
        assertEquals("index", result);
    }

    @Test
    public void testGetAccountPage() {
        String result = navigationController.getAccountPage();
        assertEquals("account", result);
    }

    @Test
    public void testGetAccountsPage() {
        String result = navigationController.getAccountsPage();
        assertEquals("accounts", result);
    }

    @Test
    public void testGetDashboardPage() {
        String result = navigationController.getDashboardPage();
        assertEquals("dashboard", result);
    }

    @Test
    public void testGetLoginPage() {
        String result = navigationController.getLoginPage();
        assertEquals("login", result);
    }

    @Test
    public void testGetPaymentPage() {
        String result = navigationController.getPaymentPage();
        assertEquals("payment", result);
    }

    @Test
    public void testGetPaymentsPage() {
        String result = navigationController.getPaymentsPage();
        assertEquals("payments", result);
    }

    @Test
    public void testGetProfilePage() {
        String result = navigationController.getProfilePage();
        assertEquals("profile", result);
    }

    @Test
    public void testGetRegistrationPage() {
        String result = navigationController.getRegistrationPage();
        assertEquals("registration", result);
    }

    @Test
    public void testGetUserPage() {
        String result = navigationController.getUserPage();
        assertEquals("user", result);
    }

    @Test
    public void testGetUsersPage() {
        String result = navigationController.getUsersPage();
        assertEquals("users", result);
    }

    @Test
    public void testGet500() {
        String result = navigationController.get500();
        assertEquals("/error/500", result);
    }
}
