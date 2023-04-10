package com.project.ribbon.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String message;
    @Column
    private String profileimage;

    @Column(nullable = false)
    private LocalDateTime timestamp;


    // ChatMessage 클래스를 ChatMessageEntity 클래스로 매핑하는 생성자
    public ChatMessageEntity(ChatMessage chatMessage) {
        this.nickname = chatMessage.getNickname();
        this.message = chatMessage.getMessage();
        this.profileimage = chatMessage.getProfileimage();
        this.timestamp = chatMessage.getTimestamp();
    }
}
