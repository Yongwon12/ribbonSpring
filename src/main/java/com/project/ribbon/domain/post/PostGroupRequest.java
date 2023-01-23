package com.project.ribbon.domain.post;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGroupRequest {

        private Integer id;
        private String region;
        private String title;
        private String line;
        private String description;
        private String peoplenum;
        private String gender;
        private String minage;
        private String titleimage;
        private Long userid;   // PK
        private String maxage;
        private String writedate;
        private String peoplenownum;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        private Long groupid;
        private String once;
        private Integer commentcount;

}

