package com.example.projectv1.service;

import com.example.projectv1.entity.User;
import com.example.projectv1.entity.UserRepository;
import com.example.projectv1.request.ForgotPasswordRequest;
import com.example.projectv1.response.GlobalResponse;
import com.example.projectv1.response.UserResponse;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    private ResponseEntity<?> sendResetEmail(String email, String resetToken) {
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        String emailBody = "Click the link below to reset your password:\n" + resetLink;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset");
        mailMessage.setText(emailBody);
        try {
            javaMailSender.send(mailMessage);
            updateResetPasswordToken(resetToken, email);
        } catch (MailException e) {
            return GlobalResponse.responseHandler("Failed to send mail.", HttpStatus.BAD_REQUEST, UserResponse.builder().build());
        }
        return GlobalResponse.responseHandler("Email Sent", HttpStatus.OK, UserResponse.builder().build());
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        if (!(userRepository.existsUserByEmail(forgotPasswordRequest.getEmail()))) {
            return GlobalResponse.responseHandler("Email doesn't exist", HttpStatus.BAD_REQUEST, UserResponse.builder().build());
        }

        String resetToken = RandomString.make(30);
        sendResetEmail(forgotPasswordRequest.getEmail(), resetToken);

        return GlobalResponse.responseHandler("Reset password link sent successfully to the email", HttpStatus.OK, UserResponse.builder().build());
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1);
        userOptional.ifPresentOrElse(
                user -> {
                    user.setResetPasswordToken(token);
                    user.setResetPasswordTokenExpiry(expiryTime);
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("Could not find any user with the email " + email);
                }
        );
    }

    public User getByResetPasswordToken(String token) {
        try{
            User user = userRepository.findByResetPasswordToken(token);
            return user;
        } catch (NullPointerException e){
            return null;
        }
    }

    public Boolean isResetTokenValid(LocalDateTime expiryTime){
        return LocalDateTime.now().isBefore(expiryTime);
    }

    public ResponseEntity<?> resetPassword(String token, String newPassword, String confirmPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = getByResetPasswordToken(token);

        if (newPassword.equals(confirmPassword) && user != null) {
            if (isResetTokenValid(user.getResetPasswordTokenExpiry())) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setResetPasswordToken(null);
                user.setResetPasswordTokenExpiry(null);
                userRepository.save(user);
                return GlobalResponse.responseHandler("Password successfully changed", HttpStatus.OK, UserResponse.builder().email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).build());
            } else {
                return GlobalResponse.responseHandler("Token Expired",HttpStatus.BAD_REQUEST,UserResponse.builder().build());
            }
        } else if (!(newPassword.equals(confirmPassword))) {
            return GlobalResponse.responseHandler("Passwords do not match!" ,HttpStatus.BAD_REQUEST,UserResponse.builder().build());
        } else {
            return GlobalResponse.responseHandler("Invalid token" ,HttpStatus.BAD_REQUEST,UserResponse.builder().build());
        }
    }
}
