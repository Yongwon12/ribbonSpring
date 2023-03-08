package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUserUpdateRequest {

        private String sns;
        @Size(min = 2, max = 10, message = "닉네임은 2~10자리여야 합니다.")
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;
        @NotNull(message = "수정날짜는 필수 입력값입니다.")
        private String modifydate;
        private String bestcategory;
        @Size(min = 2, max = 20, message = "한 줄 설명은 2~20자리여야 합니다.")
        private String shortinfo;
        //private String youtube;
        private String profileimage;
        @NotNull(message = "유저아이디는 필수 입력값입니다.")
        private Long userid; //PK


}

