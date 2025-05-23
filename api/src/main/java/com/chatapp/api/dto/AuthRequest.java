package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
