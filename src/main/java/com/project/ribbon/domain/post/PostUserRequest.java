package com.project.ribbon.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRequest {

        private Long id; //PK
        private String username;
        private String password;
        private String email;
        private String nickname;
        private String mobile;
        private String createdate;
        private String modifydate;
        private String birth;
        private String userimage;
        private String gender;
        private String bestcategory;
        private String shortinfo;

}

