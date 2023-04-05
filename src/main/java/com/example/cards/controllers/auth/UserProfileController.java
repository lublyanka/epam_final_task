package com.example.cards.controllers.auth;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public ResponseEntity<?> loadUserProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        User user = userRepository.findByEmailWithCountry(jwtTokenUtil.extractUsername(token));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        User userToSave = userRepository.findById(updatedUser.getId()).orElse(null);

        if (userToSave == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

        User userByEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (userByEmail != null
                && !Objects.equals(userByEmail.getId(), userToSave.getId())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (!userToSave.getName().equals(updatedUser.getName()))
            userToSave.setName(updatedUser.getName());

        if (!userToSave.getSurname().equals(updatedUser.getSurname()))
            userToSave.setSurname(updatedUser.getSurname());

        if (!userToSave.getEmail().equals(updatedUser.getEmail()))
            userToSave.setEmail(updatedUser.getEmail());

        if (!userToSave.getPhone().equals(updatedUser.getPhone()))
            userToSave.setPhone(updatedUser.getPhone());

        if (!userToSave.getUserAddress()
                .equals(updatedUser.getUserAddress()))
            userToSave.setUserAddress(updatedUser.getUserAddress());


        userToSave.setUpdateOn(Timestamp.from(Instant.now()));

        userToSave = userRepository.save(userToSave);
        userRepository.flush();
        return ResponseEntity.ok(userToSave);//.build();

    }

}
