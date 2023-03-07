package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReportCommentsResponse {

   private Long commentsid;
    private Long userid;
    private String description;
    private Long inherentid;


}