package com.example.cards.services;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User getUserByToken(String token) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
        return user;
    }
}
