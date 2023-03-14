package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostGroupCommentsResponse {

    private String description;
    private Long userid;
    private String  nickname;
    private Long inherentid;
    private String writedate;
    private Long commentsid;
    private String profileimage;
    private Integer commentscategory;

}