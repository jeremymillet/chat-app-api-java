package com.chatapp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name ="name")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(updatable = false,name="created_at")
    private Long createdAt;
    
    @Column(name="refresh_token")
    private String refreshToken;

    @Column(nullable = true)
    private String description;

    @Column(name="profile_picture",nullable = true)
    private String profilePicture;

    @Column(name="profile_picture_public_id",nullable = true)
    private String profilePicturePublicId;

}
