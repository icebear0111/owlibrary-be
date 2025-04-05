package com.owlibrary.user.controller;

import com.owlibrary.user.dto.*;
import com.owlibrary.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@RequestBody @Valid SignupRequest request) {
        SignupResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find-username")
    public ResponseEntity<FindUsernameResponse> findUsername(@RequestBody @Valid FindUsernameRequest request) {
        return ResponseEntity.ok(userService.findUsername(request));
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<Void> requestResetCode(@RequestBody @Valid PasswordResetRequest request) {
        userService.sendResetPasswordCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/verify")
    public ResponseEntity<Void> verifyResetCode(@RequestBody @Valid PasswordResetCodeVerifyRequest request) {
        userService.verifyResetPasswordCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody @Valid PasswordResetConfirmRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

    @PatchMapping("/me/profile")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileRequest request, Authentication authentication) {
        String username = authentication.getName();
        userService.updateProfileImage(username, request);
        return ResponseEntity.ok().build();
    }
}
