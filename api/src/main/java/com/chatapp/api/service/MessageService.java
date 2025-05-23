package com.chatapp.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.api.dto.MessageDTO;
import com.chatapp.api.dto.MessageRequest;
import com.chatapp.api.entity.Conversation;
import com.chatapp.api.entity.Message;
import com.chatapp.api.repository.ConversationRepository;
import com.chatapp.api.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public Message sendMessage(MessageRequest messageRequest){
        Message message = convertToMessage(messageRequest);
        messageRepository.save(message);

        Conversation conversation = conversationRepository.findById(messageRequest.getConversationId())
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        conversation.setLastMessage(messageRequest.getContent());
        conversation.setLastActive(LocalDateTime.now());
        conversationRepository.save(conversation);

        return message;
    }

    public List<MessageDTO> getMessages(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
        // Conversion de Message en MessageDTO
        return messages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.setIsRead(true);
        messageRepository.save(message);
    }

    public MessageDTO convertToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setConversationId(message.getConversationId());
        messageDTO.setSenderId(message.getSenderId());
        messageDTO.setContent(message.getContent());

        // Convertir le timestamp (Long) en LocalDateTime
        LocalDateTime timestamp = Instant.ofEpochMilli(message.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        messageDTO.setTimestamp(timestamp);

        messageDTO.setIsRead(message.isRead());
        return messageDTO;
    }
    
    private Message convertToMessage(MessageRequest messageRequest) {
        Message message = new Message();
        message.setConversationId(messageRequest.getConversationId());
        message.setSenderId(messageRequest.getSenderId());
        message.setContent(messageRequest.getContent());
        message.setTimestamp(System.currentTimeMillis()); // Ajoute un timestamp si nécessaire
        return message;
    }
}
