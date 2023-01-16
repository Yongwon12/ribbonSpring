package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostMyUsedResponse {

    private Integer id;
    private String region;
    private String title;
    private String description;
    private String usedimage1;
    private String price;
    private Long userid;
    private String writedate;
    private String nickname;
    private Long usedid;      // PK
    private String  usedimage2;
    private String  usedimage3;
    private String  usedimage4;
    private String  usedimage5;
    private Integer  likedcount;
    private Integer  commentcount;
    private String profileimage;
}