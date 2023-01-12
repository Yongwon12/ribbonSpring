package com.project.ribbon.domain.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private Integer id;             // PK
    private Long userid;        // 제목
    private String title;      // 내용
    private String description;       // 작성자
    private String img;
    private String writedate;
    private String profileimage;
    private String nickname;
    private Long boardid;
    private Integer likedcount;
    private Integer commentcount;
}