package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReplyResponse {

    private String description;

    private Long userid;
    private String  nickname;
    private Long categoryid;
    private Long inherentid;
    private String writedate;
    private Long replyid;
    private Long inherentcommentsid;
    private Integer isrecomment;
    private String profileimage;


}