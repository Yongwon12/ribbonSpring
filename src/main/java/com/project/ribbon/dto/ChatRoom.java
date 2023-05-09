package com.project.ribbon.dto;

import com.project.ribbon.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String roomName;
    private Long myid;
    private Long yourid;
    private LocalDateTime createdate;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String roomName, Long myid, Long yourid,LocalDateTime createdate) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.myid = myid;
        this.yourid = yourid;
        this.createdate = createdate;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 채팅방을 들어왔습니다.");
            sessions.add(session);
        } if (chatMessage.getType().equals(ChatMessage.MessageType.EXIT)) {
            try {
                sessions.remove(session);
                session.close();
                chatMessage.setMessage(chatMessage.getSender() + "님이 채팅방을 나갔습니다.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        sendMessage(chatMessage, chatService);

    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}
