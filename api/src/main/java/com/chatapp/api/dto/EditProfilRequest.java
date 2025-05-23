package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditProfilRequest {
    private String username;
    private String description;
}
