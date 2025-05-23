package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRegisterRequest {
    private String email;
    private String username;
    private String password;
}
