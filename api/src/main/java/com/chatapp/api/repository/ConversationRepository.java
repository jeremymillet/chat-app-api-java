package com.chatapp.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.api.entity.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    Optional<Conversation> findByFriendshipId(Long friendshipId);
    void deleteByFriendshipId(Long friendshipId);
    
}
