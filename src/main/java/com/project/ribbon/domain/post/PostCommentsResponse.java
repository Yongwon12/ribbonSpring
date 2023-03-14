package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCommentsResponse {

    private String description;
    private String writedate;
    private Long userid;
    private String  nickname;
    private Integer categoryid;
    private Long inherentid;

    private Long commentsid;
    private String profileimage;
    private Integer commentscategory;


}