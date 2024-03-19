package com.example.projectv1.service;

import com.example.projectv1.entity.Role;
import com.example.projectv1.entity.User;
import com.example.projectv1.entity.UserRepository;
import com.example.projectv1.request.AuthenticationRequest;
import com.example.projectv1.response.AuthenticationResponse;
import com.example.projectv1.request.RegisterRequest;
import com.example.projectv1.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    Object object = new Object();

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        String message;
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            message = "Email " + registerRequest.getEmail() + " already in use";
            object = AuthenticationResponse.builder().build();
            return GlobalResponse.responseHandler(message, HttpStatus.BAD_REQUEST, object);
        }

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        if (!(user.getEmail().endsWith("@gmail.com"))) {
            message = "Wrong email format!";
            object = AuthenticationResponse.builder().build();
            return GlobalResponse.responseHandler(message, HttpStatus.BAD_REQUEST, object);
        }

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        message = "Account Successfully Registered";
        object = AuthenticationResponse.builder().token(jwtToken).build();
        return GlobalResponse.responseHandler(message, HttpStatus.OK, object);
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            var user = userRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found")); // Handle user not found

            var jwtToken = jwtService.generateToken(user);

            String message = "User authenticated successfully";
            object = AuthenticationResponse.builder().token(jwtToken).build();
            return GlobalResponse.responseHandler(message, HttpStatus.OK, object);
        } catch (AuthenticationException e) {
            // Handle authentication failure, e.g., invalid credentials
            String errorMessage = "Authentication failed: " + e.getMessage();
            object = AuthenticationResponse.builder().build();
            return GlobalResponse.responseHandler(errorMessage, HttpStatus.UNAUTHORIZED, object);
        }
    }

}
