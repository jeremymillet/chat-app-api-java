package com.chatapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMessageRequest {
    private Long senderId;

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    private Long conversationId;

}
