package com.owlibrary.user.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequest {
    private String username;
    private String email;
    private String phone;
}