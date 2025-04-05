package com.owlibrary.user.dto;

import lombok.Getter;

@Getter
public class PasswordResetCodeVerifyRequest {
    private String username;
    private String code;
}