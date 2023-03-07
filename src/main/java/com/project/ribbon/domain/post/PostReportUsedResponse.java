package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReportUsedResponse {

   private Long usedid;
    private Long userid;
    private String title;
    private String description;
    private String usedimage1;
    private String usedimage2;
    private String usedimage3;
    private String usedimage4;
    private String usedimage5;


}