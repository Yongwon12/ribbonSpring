package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Pattern;
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
        private Long userid;
        private String writedate;
        private String maxage;
        private String minage;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        @Id
        private Long individualid;      // PK
        private Integer likedcount;
        private Integer commentcount;

}

