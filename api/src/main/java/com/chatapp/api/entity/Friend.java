package com.chatapp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "user_id")
    private Long userId;

    @Column(nullable = false,name="friend_id")
    private Long friendId;

    private boolean accepted;

    public Friend(Long userId, Long friendId, boolean accepted) {
        this.userId = userId;
        this.friendId = friendId;
        this.accepted = accepted;
    }
}
