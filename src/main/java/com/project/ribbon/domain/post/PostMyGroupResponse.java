package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostMyGroupResponse {

    private Integer id;
    private String region;
    private String title;
    private String line;
    private String description;
    private String peoplenum;
    private String gender;
    private String minage;
    private String titleimage;
    private Long userid;
    private String maxage;
    private String writedate;
    private String peoplenownum;
    private String nickname;
    private Long groupid;   // PK
    private String once;
    private Integer commentcount;
}