package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getToken(User user) {
        String token = jwtTokenUtil.generateToken(user);
        user.setUpdatedOn(Timestamp.from(Instant.now()));
        user.setLastLogin(Timestamp.from(Instant.now()));
        userRepository.save(user);
        userRepository.flush();
        return token;
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByToken(String token) {
        return userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
    }

    public User getUserByTokenWithCountry(String token) {
        return userRepository.findByEmailWithCountry(jwtTokenUtil.extractUsername(token));
    }

    public User updateUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("user");
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    public User updateUser(User updatedUser, User userToSave) {
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

        userToSave.setUpdatedOn(Timestamp.from(Instant.now()));

        userToSave = userRepository.save(userToSave);
        userRepository.flush();
        return userToSave;
    }

    public void block(User user) {
        if (user.isEnabled()) {
            user.setBlocked(true);
            user.setUpdatedOn(Timestamp.from(Instant.now()));
            userRepository.save(user);
            userRepository.flush();
        }
    }

    public void unblock(User user) {
        if (!user.isEnabled()) {
            user.setBlocked(false);
            user.setUpdatedOn(Timestamp.from(Instant.now()));
            userRepository.save(user);
            userRepository.flush();
        }
    }

    public boolean isExistsByEmail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
    public boolean isPasswordMatch(String password, User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        return encoder.matches(password, user.getPassword());
    }



}
