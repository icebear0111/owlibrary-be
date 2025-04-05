package com.owlibrary.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetConfirmRequest {

    @NotBlank
    private String newPassword;

    @NotBlank
    private String passwordConfirm;
}