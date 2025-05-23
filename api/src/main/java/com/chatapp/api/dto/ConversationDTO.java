package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationDTO {
    private Long conversationId;
    private Long friendshipId;

}