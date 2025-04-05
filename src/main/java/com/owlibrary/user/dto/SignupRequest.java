package com.owlibrary.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^01[016789][0-9]{7,8}$", message = "형식이 올바르지 않습니다.")
    private String phone;
}
