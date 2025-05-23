package com.chatapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.api.dto.FriendDTO;
import com.chatapp.api.entity.Friend;
import com.chatapp.api.service.FriendService;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<FriendDTO>> getFriends(@PathVariable Long userId) {
        List<FriendDTO> friends = friendService.getAllFriends(userId);
        System.out.println(friends);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{userId}/friend/{friendshipId}")
    public ResponseEntity<FriendDTO> getFriend(@PathVariable Long userId, @PathVariable Long friendshipId) {
        FriendDTO friend = friendService.getFriend(userId,friendshipId);
        System.out.println(friend);
        return ResponseEntity.ok(friend);
    }

    @GetMapping("/{userId}/friendsWithConversations")
    public ResponseEntity<List<FriendDTO>> getFriendsWithConversations(@PathVariable Long userId) {
        List<FriendDTO> friends = friendService.getAllFriendsWithConversation(userId);
        System.out.println(friends);
        return ResponseEntity.ok(friends);
    }
    
    @PostMapping("/send-request")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
        try {
            Friend friendRequest = friendService.sendFriendRequest(userId, friendId);
            
            return ResponseEntity.ok("Friend request sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API pour accepter une demande d'amitié
    @PostMapping("/accept-request")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
        try {
            Friend friendRequest = friendService.acceptFriendRequest(userId, friendId);
            return ResponseEntity.ok("Friend request accepted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFriend(@RequestParam Long friendShipId) {
        try {
            friendService.removeFriend(friendShipId);
            return ResponseEntity.ok("Friendship deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting friendship: " + e.getMessage());
        }
    }
    
}
