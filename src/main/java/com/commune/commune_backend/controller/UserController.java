package com.commune.commune_backend.controller;

import com.commune.commune_backend.model.*;
import com.commune.commune_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // it tells spring that this class will handle http requests from the frontend
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register") // this means when the backend receives a POST request at /register, run this method(registerUser)
    public User registerUser(@Valid @RequestBody User user){ // it converts the data sent by the frontend into a User Java Object
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request.getUsername(), request.getPassword());
    }

    @GetMapping("/profile")
    public ProfileResponse profile(Authentication authentication){
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(Authentication authentication, @RequestBody User updatedUser) {
        String username = authentication.getName();
        return userService.updateProfile(username, updatedUser);
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestBody VerificationRequest request) {

        boolean verified = userService.verifyEmail(
                request.getEmail(),
                request.getCode()
        );

        if (verified) {
            return "Email verified successfully";
        }

        return "Invalid or expired verification code";
    }
}
