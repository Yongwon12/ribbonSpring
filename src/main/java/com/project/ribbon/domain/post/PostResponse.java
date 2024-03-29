package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    private String board;
    private Integer id;             // PK
    private Long userid;        // 제목
    private String title;      // 내용
    private String description;       // 작성자
    private String img;
    private String writedate;
    private String nickname;
    private Long boardid;
    private Integer likedcount;
    private Integer commentcount;
    private String profileimage;
    private String token;
    private Integer inquirycount;
}