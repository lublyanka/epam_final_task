package com.example.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** The type Navigation controller. */
@Controller
public class NavigationController {
  /**
   * Gets home html page.
   *
   * @return the index
   */
  @GetMapping("")
  public String getIndex() {
    return "index";
  }

  /**
   * Gets account html page.
   *
   * @return the account page
   */
  @GetMapping(value = "/account/{accountId}")
  public String getAccountPage() {
    return "account";
  }

  /**
   * Gets accounts html page.
   *
   * @return the accounts page
   */
  @GetMapping("/accounts")
  public String getAccountsPage() {
    return "accounts";
  }

  /**
   * Gets dashboard html page.
   *
   * @return the dashboard page
   */
  @GetMapping("/dashboard")
  public String getDashboardPage() {
    return "dashboard";
  }

  /**
   * Gets login html page.
   *
   * @return the login page
   */
  @GetMapping("/login")
  public String getLoginPage() {
    return "login";
  }

  /**
   * Gets payment html page.
   *
   * @return the payment page
   */
  @GetMapping(value = "/payment/{paymentId}")
  public String getPaymentPage() {
    return "payment";
  }

  /**
   * Gets payments html page.
   *
   * @return the payments page
   */
  @GetMapping("/payments")
  public String getPaymentsPage() {
    return "payments";
  }

  /**
   * Gets profile html page.
   *
   * @return the profile page
   */
  @GetMapping("/profile")
  public String getProfilePage() {
    return "profile";
  }

  /**
   * Gets registration html page.
   *
   * @return the registration page
   */
  @GetMapping("/register")
  public String getRegistrationPage() {
    return "registration";
  }

  /**
   * Gets user html page.
   *
   * @return the user page
   */
  @GetMapping(value = "/users/{userId}")
  public String getUserPage() {
    return "user";
  }

  /**
   * Gets users html page.
   *
   * @return the users page
   */
  @GetMapping("/users")
  public String getUsersPage() {
    return "users";
  }

  /**
   * Gets 500 error html page.
   *
   * @return the 500
   */
  @GetMapping("/Error500")
  public String get500() {
    return "/error/500";
  }
}
