package com.owlibrary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUsernameResponse {
    private String maskedUsername;
}