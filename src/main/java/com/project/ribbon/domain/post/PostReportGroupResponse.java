package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReportGroupResponse {

   private Long groupid;
    private Long userid;
    private String title;
    private String description;
    private String titleimage;


}