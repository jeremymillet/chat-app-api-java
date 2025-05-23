package com.chatapp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private Long friendshipId;
    private Long friendId;
    private Long requestSenderId;
    private String friendName;
    private Long createdAt;
    private Boolean isAccepted;

}
