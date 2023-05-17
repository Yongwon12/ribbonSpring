package com.project.ribbon.controller;

import com.project.ribbon.domain.post.PostChatRoomDeleteRequest;
import com.project.ribbon.domain.post.PostChatRoomResponse;
import com.project.ribbon.dto.RoomRequest;
import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.ChatService;
import com.project.ribbon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final PostService postService;

//     채팅방 아이디 발급
@PostMapping
public ResponseEntity<Map<String, Object>> createRoom(@RequestBody RoomRequest roomRequest) {
    Map<String, Object> obj = new HashMap<>();
    try {
        chatService.createRoom(roomRequest.getRoomName(), roomRequest.getMyid(), roomRequest.getYourid());
        PostChatRoomResponse posts = postService.findPostByMyId(roomRequest.getMyid());
        Map<String, Object> roomInfo = new HashMap<>();
        roomInfo.put("roomId", posts.getRoomid());
        roomInfo.put("roomName", posts.getRoomname());
        obj.put("chatroom", roomInfo);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    } catch (Exception e) {
        // Handle the exception here
        System.err.println("Error occurred while creating room");
        System.err.println(e.getMessage());
        obj.put("error", e.getMessage());
        return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
        // 특정 채팅방 조회
        @PostMapping("/post/chatroominfo")
        public ResponseEntity<?> viewChatRoomPost(@RequestBody PostChatRoomResponse myid, Model model) {
            Map<String, Object> obj = new HashMap<>();
            try {
                List<PostChatRoomResponse> posts = postService.findPostByChatRoomMyId(myid.getMyid());
                model.addAttribute("posts", posts);
                obj.put("chatroom", posts);
                return new ResponseEntity<>(obj, HttpStatus.OK);
            } catch (Exception e) {
                // Handle the exception here
                System.err.println("Error occurred while fetching posts for chatroom");
                System.err.println(e.getMessage());
                obj.put("error", e.getMessage());
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    // 특정 채팅내역 조회
    @PostMapping("/post/chatinfo")
    public ResponseEntity<?> viewChatInfoPost(@RequestBody PostChatRoomResponse roomname, Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostChatRoomResponse> posts;
        try {
            posts = postService.findByChatInfoMyIdPost(roomname.getRoomname());
            model.addAttribute("posts", posts);
            obj.put("chatinfo", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
            // 예외 처리
            obj.put("err", e.getMessage());
            return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
        }
    }
    // 특정 채팅방 삭제
    @DeleteMapping("/post/deleteroom")
    public ResponseEntity<?> deleteChatRoomPost(@RequestBody PostChatRoomDeleteRequest params){
        try {
            postService.deleteChatInfoPost(params);
            postService.deleteChatRoomPost(params);
            return ResponseEntity.ok().build();
        } catch (ApiException e) {
            // 예외 처리
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}