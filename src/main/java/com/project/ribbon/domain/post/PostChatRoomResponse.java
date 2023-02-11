package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class PostChatRoomResponse {

    @Id
    private Long id;
    private String roomname;
    private Integer myid;
    private Integer yourid;
    private String yournickname;
    private String yourprofileimage;
    private String token;
}