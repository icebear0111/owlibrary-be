package com.owlibrary.user.controller;

import com.owlibrary.user.dto.*;
import com.owlibrary.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
