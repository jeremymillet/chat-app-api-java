package com.chatapp.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.api.dto.FriendDTO;
import com.chatapp.api.entity.Conversation;
import com.chatapp.api.entity.Friend;
import com.chatapp.api.entity.User;
import com.chatapp.api.repository.ConversationRepository;
import com.chatapp.api.repository.FriendRepository;
import com.chatapp.api.repository.MessageRepository;
import com.chatapp.api.repository.UserRepository;
@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<FriendDTO> getAllFriends(Long userId) {
            List<FriendDTO> friends = friendRepository.findAllByUserId(userId).stream()
                            .map(friend -> {
                                    User friendUser = userRepository.findById(friend.getFriendId())
                                                    .orElseThrow(() -> new IllegalArgumentException(
                                                                    "Friend not found"));
                                    return new FriendDTO(
                                                    friend.getId(),
                                                    friendUser.getId(),
                                                    friend.getUserId(),
                                                    friendUser.getUsername(),
                                                    friendUser.getCreatedAt(),
                                                    friend.isAccepted());
                            })
                            .collect(Collectors.toList());

            // Récupère les amis pour lesquels l'utilisateur est le destinataire
            List<FriendDTO> friendsIam = friendRepository.findAllByFriendId(userId).stream()
                            .map(friend -> {
                                    User friendUser = userRepository.findById(friend.getUserId())
                                                    .orElseThrow(() -> new IllegalArgumentException(
                                                                    "Friend not found"));
                                    return new FriendDTO(
                                                    friend.getId(),
                                                    friendUser.getId(),
                                                    friend.getUserId(),
                                                    friendUser.getUsername(),
                                                    friendUser.getCreatedAt(),
                                                    friend.isAccepted());

                            })
                            .collect(Collectors.toList());
            List<FriendDTO> allFriends = new ArrayList<>();
            allFriends.addAll(friends);
            allFriends.addAll(friendsIam);

            return allFriends;

    }
    
    public FriendDTO getFriend(Long userId,Long friendshipId) {
        Friend friendship = friendRepository.findById(friendshipId)
                        .orElseThrow(() -> new IllegalArgumentException("Friend not found"));
        Long senderId = friendship.getUserId();
        Long receiverId = friendship.getFriendId();    
        Long friendId = senderId.equals(userId) ? receiverId : senderId;
        if (friendId.equals(userId)) {
                throw new IllegalStateException("The user ID matches both sender and receiver IDs in the friendship.");
        }
        User friend = userRepository.findById(friendId)
                        .orElseThrow(() -> new IllegalArgumentException("Friend not found"));
        
        return new FriendDTO(
                        friendship.getId(),
                        friend.getId(),
                        friendship.getUserId(),
                        friend.getUsername(),
                        friend.getCreatedAt(),
                        friendship.isAccepted());

    }
    
    public List<FriendDTO> getAllFriendsWithConversation(Long userId) {
        List<FriendDTO> friends = friendRepository.findAllByUserId(userId).stream()
                .map(friend -> {
                    User friendUser = userRepository.findById(friend.getFriendId())
                            .orElseThrow(() -> new IllegalArgumentException("Friend not found"));
                    return new FriendDTO(
                            friend.getId(),
                            friendUser.getId(),
                            friend.getUserId(),
                            friendUser.getUsername(),
                            friendUser.getCreatedAt(),
                            friend.isAccepted());
                })
                .collect(Collectors.toList());

        // Récupère les amis pour lesquels l'utilisateur est le destinataire
        List<FriendDTO> friendsIam = friendRepository.findAllByFriendId(userId).stream()
                .map(friend -> {
                    User friendUser = userRepository.findById(friend.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("Friend not found"));
                    return new FriendDTO(
                            friend.getId(),
                            friendUser.getId(),
                            friend.getUserId(),
                            friendUser.getUsername(),
                            friendUser.getCreatedAt(),
                            friend.isAccepted());

                })
                .collect(Collectors.toList());
        List<FriendDTO> allFriends = new ArrayList<>();
        allFriends.addAll(friends);
        allFriends.addAll(friendsIam);

        // Récupère les conversations avec les amis
        List<FriendDTO> friendsWithConversations = new ArrayList<>();
        for (FriendDTO friend : allFriends) {
            Optional<Conversation> conversation = conversationRepository.findByFriendshipId(friend.getFriendshipId());
            if (conversation.isPresent()) {
                friendsWithConversations.add(friend);
            }

        }
        return friendsWithConversations;

    }
    
    public Friend sendFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        // Vérifier si la demande d'amitié existe déjà
        if (friendRepository.existsByUserIdAndFriendId(userId, friendId)
                || friendRepository.existsByUserIdAndFriendId(friendId, userId)) {
            throw new IllegalArgumentException("Friend request already exists");
        }

        // Créer une nouvelle demande d'amitié
        Friend friendRequest = new Friend(userId, friendId, false); // false signifie que la demande n'est pas acceptée
        return friendRepository.save(friendRequest);
    }

    // Accepter la demande d'amitié
    public Friend acceptFriendRequest(Long userId, Long friendId) {
        System.out.println(userId);
        System.out.println(friendId);
        Friend friendRequest = friendRepository.findByUserIdAndFriendIdAndAccepted(friendId, 
                userId, false)
        .orElseThrow(() -> new IllegalArgumentException("No pending friend request found"));
        if (friendRequest.getFriendId() == userId) {
            System.out.println(userId);
            System.out.println(friendId);
            System.out.println(friendRequest);
            friendRequest.setAccepted(true);
            return friendRepository.save(friendRequest);
        }
        else {
            throw new IllegalArgumentException("vous avez deja envoyée une demande d'amitié");
        }
        
    }
    
    @Transactional
    public void removeFriend(Long friendShipId) {
        Optional<Conversation> conversation = conversationRepository.findByFriendshipId(friendShipId);
        if (conversation.isPresent()) {
        Long conversationId = conversation.get().getId();
        messageRepository.deleteAllByConversationId(conversationId);
        conversationRepository.deleteByFriendshipId(friendShipId);
    }

    friendRepository.deleteById(friendShipId);

    }
}
