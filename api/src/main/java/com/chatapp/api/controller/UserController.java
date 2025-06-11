package com.chatapp.api.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.api.dto.EditProfilRequest;
import com.chatapp.api.dto.UserDTO;
import com.chatapp.api.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }
    
    @PatchMapping("/{userId}")
    public ResponseEntity<Map<String, String>> patchUserProfil(@PathVariable Long userId,
            @RequestBody EditProfilRequest body) {
        System.out.println(body);
        userService.editUser(userId, body.getUsername(), body.getDescription());
        Map<String, String> response = Collections.singletonMap("message", "User updated successfully");
        return ResponseEntity.ok(response);
    }
}
