package com.project.ribbon.dto;

import lombok.Data;

@Data
public class ChatMessage {
    public enum MessageType{
        ENTER,TALK,EXIT
    }

    private MessageType type;
    private Long id;
    private String roomId;
    private String roomName;
    private Long myid;
    private Long yourid;
    private String sender;
    private String message;
    private String isright;
    private String token;
}