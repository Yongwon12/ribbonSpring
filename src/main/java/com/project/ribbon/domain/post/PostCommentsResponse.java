package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostCommentsResponse {

    private String description;
    private String writedate;
    private Integer userid;
    private String  nickname;
    private Integer categoryid;
    private Long inherentid;
    private Long commentsid;
    private Integer isrecomment;
    private String profileimage;
    private Integer commentcategory;


}