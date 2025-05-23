package com.chatapp.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.api.entity.Friend;


public interface FriendRepository extends JpaRepository<Friend,Long>{

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    List<Friend> findAllByUserId(Long userId);

    List<Friend> findAllByFriendId(Long userId);
    
    Optional<Friend> findById(Long id);
    Optional<Friend> findByUserIdAndFriendIdAndAccepted(Long userId, Long friendId, boolean accepted);

    Friend findByUserIdAndFriendId(Long userId, Long friendId);
    
    void deleteById(Long friendShipId);
} 
