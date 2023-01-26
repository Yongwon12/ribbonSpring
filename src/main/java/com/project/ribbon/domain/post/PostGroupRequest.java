package com.project.ribbon.domain.post;

import jakarta.validation.constraints.NotBlank;
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
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,30}$", message = "제목은 특수문자를 제외한 최소 2자, 최대 30자입니다.")
        @NotBlank(message="제목은 필수 입력 값입니다.")
        private String title;
        @NotBlank(message="내용은 필수 입력 값입니다.")
        private String description;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,30}$", message = "한 줄 설명은 특수문자를 제외한 최대 30자입니다.")
        private String line;
        private Integer peoplenum;
        private String gender;
        private Integer minage;
        private String titleimage;
        private Long userid;
        private Integer maxage;
        private String writedate;
        private Integer peoplenownum;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @NotBlank(message="닉네임은 필수 입력 값입니다.")
        private String nickname;
        private Long groupid;// PK
        private String once;
        private Integer commentcount;

}

