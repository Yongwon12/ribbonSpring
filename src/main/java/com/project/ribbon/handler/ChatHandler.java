package com.project.ribbon.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ribbon.dto.ChatMessage;
import com.project.ribbon.dto.ChatMessageEntity;
import com.project.ribbon.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private ChatRepository chatRepository;

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // ChatHandler 클래스의 handleTextMessage 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessage chatMessage = mapper.readValue(message.getPayload(), ChatMessage.class);
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity(chatMessage);
        chatRepository.save(chatMessageEntity);
        sendMessageToAll(chatMessage);
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

    private void sendMessageToAll(ChatMessage chatMessage) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(chatMessage.toString()));
            }
        }
    }
}
