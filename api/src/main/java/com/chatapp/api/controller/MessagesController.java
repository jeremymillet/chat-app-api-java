package com.chatapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.api.dto.MessageDTO;
import com.chatapp.api.dto.MessageRequest;
import com.chatapp.api.entity.Message;
import com.chatapp.api.service.ConversationService;
import com.chatapp.api.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {
        // Envoie d'un message via WebSocket à tous les participants
        Message savedMessage = messageService.sendMessage(messageRequest);
        messagingTemplate.convertAndSend(
            "/queue/messages/" +messageRequest.getConversationId(), // Destination de message sur WebSocket
            messageService.convertToMessageDTO(savedMessage)
                
        ); // Le message envoyé
        return ResponseEntity.ok().body("Message sent successfully");
    }
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long conversationId) {
        List<MessageDTO> messages = messageService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }


}


