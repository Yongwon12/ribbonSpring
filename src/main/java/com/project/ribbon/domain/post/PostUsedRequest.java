package com.project.ribbon.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedRequest {

        private Integer id;
        private String region;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,30}$", message = "제목은 특수문자를 제외한 최대 30자입니다.")
        @NotBlank(message="제목은 필수 입력 값입니다.")
        private String title;
        @NotBlank(message="내용은 필수 입력 값입니다.")
        private String description;
        private String usedimage1;
        @Positive
        @NotBlank(message="가격은 필수 입력 값입니다.")
        private Integer price;
        private Long userid;
        private String writedate;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @NotBlank(message="닉네임은 필수 입력 값입니다.")
        private String nickname;
        private Long usedid;      // PK
        private String  usedimage2;
        private String  usedimage3;
        private String  usedimage4;
        private String  usedimage5;
        private Integer  likedcount;
        private Integer  commentcount;

}

