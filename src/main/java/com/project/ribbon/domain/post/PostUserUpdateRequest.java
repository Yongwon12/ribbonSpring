package com.project.ribbon.domain.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserUpdateRequest {

        private String sns;
        private String nickname;
        private String modifydate;
        private String bestcategory;
        private String shortinfo;
        private String youtube;
        private String profileimage;
        private String userid; //PK


}

