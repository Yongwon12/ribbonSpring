package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType{
        TALK
    }

    private MessageType type;
    private String roomid;
    private String nickname;
    private String message;
    private String profileimage;
}