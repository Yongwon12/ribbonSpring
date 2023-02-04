package com.project.ribbon.domain.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostChatMessage {

    private String roomname;
    private String nickname;
    private String message;
    private String profileimage;
    private LocalDateTime writedate;

}