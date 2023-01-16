package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostGroupReplyResponse {

    private String description;
    private String writedate;
    private Long userid;
    private String  nickname;
    private Long inherentid;
    private Integer likedcount;
    private Long groupreplyid;
    private Long inherentcommentsid;
    private Integer isrecomment;
    private String profileimage;
}