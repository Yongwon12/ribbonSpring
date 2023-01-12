package com.project.ribbon.domain.post;

import lombok.Getter;

@Getter
public class PostUserResponse {

    private Long id; //PK
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String mobile;
    private String createdate;
    private String modifydate;
    private String birth;
    private String userimage;
    private String gender;
    private String bestcategory;
    private String shortinfo;
}