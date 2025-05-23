package com.chatapp.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
