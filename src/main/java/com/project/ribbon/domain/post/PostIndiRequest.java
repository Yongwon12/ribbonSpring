package com.project.ribbon.domain.post;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIndiRequest {

        private Integer id;
        private String region;
        private String meetdate;
        private String title;
        private String description;
        private String gender;
        private String userimage;
        private Long userid;
        private String writedate;
        private String maxage;
        private String minage;
        private String nickname;
        private Long individualid;      // PK
        private Integer likedcount;
        private Integer commentcount;

}

