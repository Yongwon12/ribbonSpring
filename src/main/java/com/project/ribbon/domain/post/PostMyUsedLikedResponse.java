package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMyUsedLikedResponse {

    private Integer id;
    private String region;
    private String detailregion;
    private String title;
    private String description;
    private String usedimage1;
    private String price;
    private Long userid;
    private String username;
    private String shortdescription;
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
    private String productname1;
    private String productname2;
    private String productname3;
    private String productname4;
    private String productname5;
    private String productname6;
    private String productname7;
    private String productname8;
    private String productname9;
    private String productname10;
    private String price1;
    private String price2;
    private String price3;
    private String price4;
    private String price5;
    private String price6;
    private String price7;
    private String price8;
    private String price9;
    private String price10;
    private String storename;
    private String numpeople;
    private String opentime;
    private String storetel;
    private String parking;
    private String holiday;

}