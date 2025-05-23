package com.chatapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.api.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);
    void deleteAllByConversationId(Long conversationId);
}
