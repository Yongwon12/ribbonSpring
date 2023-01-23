package com.project.ribbon.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRequest {

        private Long userid; //PK
        private String username;
        private String sns;

        private String email;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
        private String mobile;
        private String createdate;
        private String modifydate;
        private String birth;
        private String profileimage;
        private String gender;
        private String bestcategory;
        private String shortinfo;
        private String youtube;
        private String token;

}
