package com.project.ribbon.dto;

import lombok.Data;

@Data
public class ChatMessage {
    public enum MessageType{
        ENTER,TALK,EXIT
    }

    private MessageType type;
    private String roomId;
    private String roomName;
    private String sender;
    private String message;
}