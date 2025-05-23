package com.chatapp.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.api.dto.ConversationDTO;
import com.chatapp.api.entity.Conversation;
import com.chatapp.api.entity.Friend;
import com.chatapp.api.repository.ConversationRepository;
import com.chatapp.api.repository.FriendRepository;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private FriendRepository friendRepository;

    public ConversationDTO getOrCreateConversation(Long friendshipId) {
        Optional<Conversation> optionalConversation = conversationRepository.findByFriendshipId(friendshipId);

        Conversation conversation = optionalConversation.orElseGet(() -> {
            // Si la conversation n'existe pas, créer une nouvelle
            Conversation newConversation = new Conversation();
            newConversation.setFriendshipId(friendshipId);
            return conversationRepository.save(newConversation);
        });

        return new ConversationDTO(conversation.getId(), conversation.getFriendshipId());
    }
    public List<Long> getParticipants(Long conversationId) {
        // Trouver la conversation par ID
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        // Récupérer l'ID de l'ami (friendshipId) de la conversation
        Long friendshipId = conversation.getFriendshipId();

        // Trouver les utilisateurs dans cette relation d'amitié
        Optional<Friend> optionalFriend = friendRepository.findById(friendshipId);

        // Récupérer les IDs des amis participants à cette conversation
        List<Long> participants = new ArrayList<>();
        if (optionalFriend.isPresent()) {
            Friend friend = optionalFriend.get();

            // Ajouter les IDs des utilisateurs participants (userId et friendId)
            participants.add(friend.getUserId());
            participants.add(friend.getFriendId());
        } else {
            throw new IllegalArgumentException("Friendship not found for the given conversation.");
        }

        return participants;
    }
}
