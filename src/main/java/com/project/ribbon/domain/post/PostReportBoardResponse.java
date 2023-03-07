package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReportBoardResponse {

   private Long boardid;
    private Long userid;
    private String title;
    private String description;
    private String img;


}