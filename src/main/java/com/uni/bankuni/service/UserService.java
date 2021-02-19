package com.uni.bankuni.service;

import com.uni.bankuni.domain.User;
import com.uni.bankuni.exception.UserAlreadyExistsException;
import com.uni.bankuni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public void registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException();
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            accountService.createAccount(user.getEmail());
        }
    }

    public User getUser(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
