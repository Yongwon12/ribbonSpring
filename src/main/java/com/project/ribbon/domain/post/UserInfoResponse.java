package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.Getter;


@Getter
public class UserInfoResponse {

    private Long userid;
    private String nickname;
    private String bestcategory;
    private String profileimage;
    private String shortinfo;
    private String sns;
    private String youtube;




}