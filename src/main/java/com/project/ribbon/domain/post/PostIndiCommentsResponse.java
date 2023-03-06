package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostIndiCommentsResponse {

    private String description;
    private Integer userid;
    private String  nickname;
    private Long inherentid;
    private String writedate;
    private Long commentsid;
    private Integer isrecomment;
    private String profileimage;
    private Integer commentcategory;

}