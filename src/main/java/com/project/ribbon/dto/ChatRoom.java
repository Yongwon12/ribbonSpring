package com.project.ribbon.dto;

import com.project.ribbon.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private final String roomId;
    private final String roomName;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        try {
            if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
                // sessions에 session이 포함되어 있는지에 대한 여부를 판단하는 로직
                // 세션이 없으면 ENTER 있으면 TALK
                if (!sessions.contains(session)) {
                    sessions.add(session);
                    chatMessage.setMessage(chatMessage.getSender() + "님이 채팅방에 입장했습니다.");
                    sendMessage(chatMessage, chatService);
                } else {
                    chatMessage.setType(ChatMessage.MessageType.TALK);
                    sendMessage(chatMessage, chatService);
                }
            } else if (chatMessage.getType().equals(ChatMessage.MessageType.TALK)) {
                sendMessage(chatMessage, chatService);
            } else if (chatMessage.getType().equals(ChatMessage.MessageType.EXIT)) {
                sessions.remove(session);
                session.close();
                chatMessage.setMessage(chatMessage.getSender() + "님이 채팅방을 나갔습니다.");
                sendMessage(chatMessage, chatService);
            } else {
                throw new IllegalArgumentException("Unsupported message type: " + chatMessage.getType());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage(), e);
        }
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                chatService.sendMessage(session, message);
            }
        }
    }
}
