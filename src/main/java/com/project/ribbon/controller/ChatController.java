package com.project.ribbon.controller;

import com.project.ribbon.domain.post.PostChatRoomResponse;
import com.project.ribbon.dto.RoomRequest;
import com.project.ribbon.service.ChatService;
import com.project.ribbon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody RoomRequest roomRequest) {
        chatService.createRoom(roomRequest.getRoomName(), roomRequest.getMyid(), roomRequest.getYourid());
        Map<String, Object> obj = new HashMap<>();
        PostChatRoomResponse posts = postService.findPostByMyId(roomRequest.getMyid());
        Map<String, Object> roomInfo = new HashMap<>();
        roomInfo.put("roomId", posts.getRoomid());
        roomInfo.put("roomName", posts.getRoomname());
        roomInfo.put("profileimage", posts.getProfileimage());
        obj.put("chatroom", roomInfo);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


}