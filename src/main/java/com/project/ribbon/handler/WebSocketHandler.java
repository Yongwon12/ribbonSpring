package com.project.ribbon.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ribbon.dto.ChatMessage;
import com.project.ribbon.dto.ChatRoom;
import com.project.ribbon.service.ChatService;
import com.project.ribbon.service.FirebaseCloudChatMessageService;
import com.project.ribbon.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final PostService postService;
    private final FirebaseCloudChatMessageService firebaseCloudChatMessageService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        // 발급받은 룸 아이디를 변수에 할당
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        // 세션 및 메세지 서비스를 핸들링
        chatRoom.handlerActions(session, chatMessage, chatService);
        // 데이터베이스에 채팅내역 저장
        postService.saveChatInfoPost(chatMessage);
        firebaseCloudChatMessageService.sendMessageTo(
                chatMessage.getToken(),
                chatMessage.getSender());
        String.valueOf(ResponseEntity.ok().build().getStatusCode());
    }
}