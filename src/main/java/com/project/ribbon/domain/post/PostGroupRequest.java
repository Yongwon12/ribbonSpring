package com.project.ribbon.domain.post;

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
        private String nickname;
        private Long groupid;
        private String once;
        private Integer commentcount;

}

