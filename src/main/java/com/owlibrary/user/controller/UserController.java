package com.owlibrary.user.controller;

import com.owlibrary.user.dto.SignupRequest;
import com.owlibrary.user.dto.SignupResponse;
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
}
