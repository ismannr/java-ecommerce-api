package com.example.projectv1.controller;

import com.example.projectv1.request.*;
import com.example.projectv1.response.AuthenticationResponse;
import com.example.projectv1.response.GlobalResponse;
import com.example.projectv1.response.UserResponse;
import com.example.projectv1.service.AuthenticationService;
import com.example.projectv1.service.ForgotPasswordService;
import com.example.projectv1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ProjectController {

    private final ForgotPasswordService forgotPasswordService;
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordMail(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return forgotPasswordService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();
        return forgotPasswordService.resetPassword(token, newPassword, confirmPassword);
    }
}
