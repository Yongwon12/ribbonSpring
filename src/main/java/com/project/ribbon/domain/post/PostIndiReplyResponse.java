package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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