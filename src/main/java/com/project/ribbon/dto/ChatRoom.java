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
                // 방의 sessions에 유저의 session이 포함되어 있는지에 대한 여부를 판단하는 로직
                // 세션이 없을때
                if (!sessions.contains(session)) {
                    sessions.add(session);
                    sendMessage(chatMessage, chatService);
                }else {
                    // 유효하지 않은 세션일 때 예외 처리
                    throw new IllegalArgumentException("Invalid session: " + session.getId());
                }
                // 클라이언트 측에서 세션을 종료하면 서버에서도 유저의 session을 sessions에서 비우고
                // 종료 하지만 방의 sessions는 유지, 하지만 서버가 재시동을 하거나 종료되면 sessions가 자동으로 remove됨
                // 따라서 기존의 룸아이디를 사용할 수 없음
            } else if (chatMessage.getType().equals(ChatMessage.MessageType.EXIT)) {
                sessions.remove(session);
                session.close();
                sendMessage(chatMessage, chatService);
            } else if (chatMessage.getType().equals(ChatMessage.MessageType.TALK)) {
                sendMessage(chatMessage, chatService);
            }else {
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
