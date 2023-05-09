package com.project.ribbon.controller;

import com.project.ribbon.dto.ChatRoom;
import com.project.ribbon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestBody String roomName,Long myid,Long yourid) {
        return chatService.createRoom(roomName,myid,yourid);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}