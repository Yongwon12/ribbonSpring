package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private Long userid;
    private String nickname;
    private String bestcategory;
    private String profileimage;
    private String shortinfo;
    private String sns;
    private String youtube;
    private String gender;




}