package com.project.ribbon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private Long id;
    private String message;
    private String nickname;
    private String profileimage;
    private LocalDateTime timestamp;
    
    public enum MessageType {
        ENTER, CHAT, LEAVE
    }

    // ChatMessage를 ChatEntity로 변환하는 메소드
    public ChatEntity toEntity() {
        ChatEntity entity = new ChatEntity();
        entity.setMessage(message);
        entity.setNickname(nickname);
        entity.setProfileimage(profileimage);
        entity.setTimestamp(timestamp);
        return entity;
    }
}