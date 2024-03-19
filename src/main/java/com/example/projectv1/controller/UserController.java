package com.example.projectv1.controller;

import com.example.projectv1.request.ChangePasswordRequest;
import com.example.projectv1.request.EditProfileRequest;
import com.example.projectv1.request.ProfileImageRequest;
import com.example.projectv1.response.ProfileImageResponse;
import com.example.projectv1.response.ProfileResponse;
import com.example.projectv1.response.UserResponse;
import com.example.projectv1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auth/authenticated")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/home")
    public ResponseEntity<?> homePage(Authentication authentication) {
        return userService.userWelcome(authentication);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> showUserProfile(Authentication authentication) {
        return userService.showProfile(authentication);
    }

    @PutMapping("/profile/edit")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileRequest editProfileRequest, Authentication authentication) {
        return userService.editProfile(editProfileRequest, authentication);
    }

    @PostMapping("/profile/edit/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest passwordRequest,
                                                       Authentication authentication) {
        return userService.changePassword(passwordRequest, authentication);
    }

    @PostMapping(value = "/edit/profile-image/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, Authentication authentication) throws IOException {
        return userService.uploadImage(file, authentication);
    }

    @GetMapping("/edit/profile-image")
    public ResponseEntity<?> profileImage(Authentication authentication) {
        return userService.showImage(authentication);
    }
}

