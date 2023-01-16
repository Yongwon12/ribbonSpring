package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostReplyResponse {

    private String description;
    private Long userid;
    private String  nickname;
    private Long categoryid;
    private Long inherentid;
    private String writedate;
    private Integer likedcount;
    private Long replyid;
    private Long inherentcommentsid;
    private Integer isrecomment;
    private String profileimage;

}