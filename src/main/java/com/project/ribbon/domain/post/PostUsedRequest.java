package com.project.ribbon.domain.post;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedRequest {

        private Integer id;
        private String region;
        private String title;
        private String description;
        private String usedimage1;
        private String price;
        private Long userid;
        private String writedate;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        private Long usedid;      // PK
        private String  usedimage2;
        private String  usedimage3;
        private String  usedimage4;
        private String  usedimage5;
        private Integer  likedcount;
        private Integer  commentcount;

}

