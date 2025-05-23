package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private Long createdAt;
    private String description;
    private String profilePicture;
}