package com.owlibrary.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @NotBlank
    private String profileImageUrl;
}
