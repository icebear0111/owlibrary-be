package com.owlibrary.user.dto;

import lombok.Getter;

@Getter
public class FindUsernameRequest {
    private String name;
    private String email;
    private String phone;
}