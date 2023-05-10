package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostChatRoomResponse {

    @Id
    private Long id;
    private String roomname;
    private String roomid;
    private Long myid;
    private Long yourid;
    private String token;
    private String writedate;
    private String writetime;
    private Integer isright;
    private String description;
    private String nickname;
    private String profileimage;
    private Long userid;
}