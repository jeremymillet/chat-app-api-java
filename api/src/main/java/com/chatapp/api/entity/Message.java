package com.chatapp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;
@Getter
@Setter
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name="conversation_id")
    private Long conversationId;

    @Column(nullable = false,name="sender_id")
    private Long senderId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = true,name="is_read")
    private boolean isRead = false;

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}