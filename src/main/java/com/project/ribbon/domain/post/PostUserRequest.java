package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUserRequest {

        private Long userid; //PK
        private String username;
        private String sns;
        @Email(message = "이메일이 잘못되었습니다.")
        private String email;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\*]{2,30}$", message = "닉네임은 특수문자를 제외한 2~30자리여야 합니다.")
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
        private String token;
        private String password;
        private Long user_userid;
        @NotNull
        @Pattern(regexp = "^USER$|^INSTRUCTOR$|^$", message = "유효한 값이 아닙니다.")
        private String roles;


}
