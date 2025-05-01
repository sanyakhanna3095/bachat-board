package com.cg.budgetboard.util;


import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserRepository userRepository;

    @Autowired
    private JwtUtility jwtUtility;

    public User getCurrentUser(String token) {
        String email = jwtUtility.extractEmail(token);
        return userRepository.findByEmail(email).orElseThrow();
    }
}
