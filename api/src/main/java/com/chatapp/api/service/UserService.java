package com.chatapp.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.chatapp.api.dto.UserDTO;
import com.chatapp.api.entity.User;
import com.chatapp.api.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return convertToUserDTO(user);
    }

    @Transactional
    public void editUser(Long id, String username, String description) {
        userRepository.patchProfile(id,username,description);
    }
    
    public UserDTO createUser(User user) {
        return convertToUserDTO(userRepository.save(user));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void findUserByEmailAndUpdateRefreshToken(Long id, String refreshToken) {
        userRepository.updateRefreshToken(refreshToken,id);
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getDescription(),
            user.getProfilePicture()
        );
    }
}
