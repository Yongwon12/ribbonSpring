package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostMyIndiResponse {

    private Integer id;
    private String region;
    private String meetdate;
    private String title;
    private String description;
    private String gender;
    private String userimage;
    private Long userid;
    private String writedate;
    private String maxage;
    private String minage;
    private String nickname;
    private Long individualid;      // PK
    private Integer likedcount;
    private Integer commentcount;

}