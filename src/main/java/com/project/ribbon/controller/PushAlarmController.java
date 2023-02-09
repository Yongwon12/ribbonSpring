package com.project.ribbon.controller;

import com.project.ribbon.domain.post.ResponseDTO;
import com.project.ribbon.service.FirebaseCloudGroupMessageCommentsService;
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
    private final FirebaseCloudGroupMessageCommentsService firebaseCloudGroupMessageCommentsService;
    @PostMapping("/api/push")
    public ResponseEntity pushMessage(@RequestBody ResponseDTO responseDTO) throws IOException {
        firebaseCloudGroupMessageCommentsService.sendMessageTo(
             );
        return ResponseEntity.ok().build();
    }
}
