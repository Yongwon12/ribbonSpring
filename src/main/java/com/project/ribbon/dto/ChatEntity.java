package com.project.ribbon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatEntity {
    private Long id;
    private String message;
    private String nickname;
    private String profileimage;
    private LocalDateTime timestamp;

}