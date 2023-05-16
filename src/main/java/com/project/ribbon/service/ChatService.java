package com.project.ribbon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ribbon.domain.post.PostChatRoomRequest;
import com.project.ribbon.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final PostService postService;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String roomName, Long myid, Long yourid) {
        String randomId = UUID.randomUUID().toString();
        PostChatRoomRequest insertparams = new PostChatRoomRequest();
        insertparams.setRoomname(roomName);
        insertparams.setMyid(myid);
        insertparams.setYourid(yourid);
        insertparams.setRoomid(randomId);
        postService.saveChatRoomPost(insertparams);
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .roomName(roomName)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}