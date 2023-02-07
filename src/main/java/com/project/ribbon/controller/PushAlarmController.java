package com.project.ribbon.controller;

import com.project.ribbon.domain.post.ResponseDTO;
import com.project.ribbon.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PushAlarmController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    @PostMapping("/api/push")
    public ResponseEntity pushMessage(@RequestBody ResponseDTO responseDTO) throws IOException {
        firebaseCloudMessageService.sendMessageTo(
                responseDTO.getToken(),
                responseDTO.getTitle(),
                responseDTO.getBody());
        return ResponseEntity.ok().build();
    }
}

