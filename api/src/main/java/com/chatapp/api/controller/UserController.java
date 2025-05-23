package com.chatapp.api.controller;

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
    public ResponseEntity<String> patchUserProfil(@PathVariable Long id, @RequestBody EditProfilRequest body) {
        userService.editUser(id,body.getUsername(),body.getDescription());
        return ResponseEntity.ok("User updated successfully");
    }
}
