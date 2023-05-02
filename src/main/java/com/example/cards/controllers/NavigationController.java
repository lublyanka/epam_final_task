package com.example.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {
    @GetMapping("")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/accounts")
    public String getAccountsPage() {
        return "accounts";
    }

    @GetMapping("/payments")
    public String getPaymentsPage() {
        return "payments";
    }

    @RequestMapping(value="/account/{accountId}")
    public String getAccountPage() {
        return "account";
    }

    @RequestMapping(value="/payment/{paymentId}")
    public String getPaymentPage() {
        return "payment";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "registration";
    }

    @GetMapping("/dashboard")
    public String getDashboardPage() {
        return "dashboard";
    }

    @GetMapping("/profile")
    public String getProfilePage() {
        return "profile";
    }

    @GetMapping("/users")
    public String getUsersPage() {
        return "users";
    }

    @RequestMapping(value="/users/{userId}")
    public String getUserPage() {
        return "user";
    }

    @GetMapping("/500test")
    public String get500test() {
        return new ResponseEntity<String>("500", null, 500).getStatusCode().toString();
    }

    @GetMapping("/Error500")
    public String get500() {
        return "/error/500";
    }

}
