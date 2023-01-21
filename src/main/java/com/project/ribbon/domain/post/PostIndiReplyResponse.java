package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostIndiReplyResponse {

    private String description;
    private String writedate;
    private Long userid;
    private String  nickname;
    private Long inherentid;
    private Long individualreplyid;
    private Long inherentcommentsid;
    private Integer isrecomment;
    private String profileimage;


}