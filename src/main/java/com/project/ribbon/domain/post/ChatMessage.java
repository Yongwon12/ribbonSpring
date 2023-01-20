package com.project.ribbon.domain.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK ,EXIT
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}