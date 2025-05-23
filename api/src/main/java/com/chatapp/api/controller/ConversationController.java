package com.chatapp.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.api.dto.ConversationDTO;
import com.chatapp.api.service.ConversationService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/{friendshipId}")
    public ResponseEntity<ConversationDTO> getFriends(@PathVariable Long friendshipId) {
        ConversationDTO conversation = conversationService.getOrCreateConversation(friendshipId);
        return ResponseEntity.ok(conversation);
    }
    
}
