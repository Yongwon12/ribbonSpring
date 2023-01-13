package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostCommentsResponse {

    private String description;
    private Integer userid;
    private String  nickname;
    private Integer categoryid;
    private Long inherentid;
    private String writedate;
    private String profileimage;
    private Integer likedcount;
    private Long commentsid;
    private Integer isrecomment;

}