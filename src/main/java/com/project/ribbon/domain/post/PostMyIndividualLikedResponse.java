package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMyIndividualLikedResponse {

    private Integer id;
    private String region;
    private String meetdate;
    private String title;
    private String description;
    private String gender;
    private Long userid;
    private String writedate;
    private String maxage;
    private String minage;
    private String nickname;
    private Long individualid;      // PK
    private Integer likedcount;
    private Integer commentcount;

    private String profileimage;
}