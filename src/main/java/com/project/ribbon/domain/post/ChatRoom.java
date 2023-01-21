package com.project.ribbon.domain.post;

import com.project.ribbon.service.ChatService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
    @OnOpen
    public void handlerActions_1(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);

    }
    @OnClose
    public void handlerActions_2(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");

        }
        sendMessage(chatMessage, chatService);
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}