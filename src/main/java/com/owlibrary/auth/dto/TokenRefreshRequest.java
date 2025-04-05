package com.owlibrary.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}
