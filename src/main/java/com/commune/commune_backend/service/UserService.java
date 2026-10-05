package com.commune.commune_backend.service;

import com.commune.commune_backend.exception.DuplicateUserException;
import com.commune.commune_backend.model.LoginResponse;
import com.commune.commune_backend.model.ProfileResponse;
import com.commune.commune_backend.model.User;
import com.commune.commune_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.commune.commune_backend.service.JwtService;

import java.time.LocalDateTime;
import java.util.Random;

@Service // it tells spring that this class contains application/business logic
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    private UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public User registerUser(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new DuplicateUserException("Email already exists");
        }

        String verificationCode = generateVerificationCode();

        user.setVerificationCode(verificationCode);
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setVerified(false);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        try {
            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getVerificationCode()
            );
        } catch (Exception exception) {
            userRepository.delete(savedUser);
            throw exception;
        }

        return savedUser;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public LoginResponse loginUser(String username, String password){
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && !user.getVerified()){
            throw new RuntimeException("Please verify your email before logging in");
        }

        if (user != null && passwordEncoder.matches(password, user.getPassword())){
            String token = jwtService.generateToken(user.getUsername());
            return new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    token
            );
        }

        return null;
    }

    public ProfileResponse updateProfile(String username, User updateUser){
        User user = userRepository.findByUsername(username).orElse(null);
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        userRepository.save(user);

        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public boolean verifyEmail(String email, String code) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return false;
        }

        if (user.getVerificationCode() == null) {
            return false;
        }

        if (user.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!user.getVerificationCode().equals(code)) {
            return false;
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationExpiresAt(null);

        userRepository.save(user);

        return true;
    }

    public boolean resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return false;
        }

        if (user.getVerified()) {
            return false;
        }

        String newCode = generateVerificationCode();

        user.setVerificationCode(newCode);
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendVerificationEmail(
                user.getEmail(),
                newCode
        );

        return true;
    }

}
