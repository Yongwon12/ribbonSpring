package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIndiRequest {

        private Integer id;
        @Size(min = 2, max = 40, message = "활동시간은 2~40자리여야 합니다.")
        private String region;
        private String meetdate;
        @Size(min = 2, max = 30, message = "제목은 2~30자리여야 합니다.")
        @NotBlank(message="제목은 필수 입력 값입니다.")
        private String title;
        @NotBlank(message="내용은 필수 입력 값입니다.")
        private String description;
        private String gender;
        private Long userid;
        private String writedate;
        private String maxage;
        private String minage;
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
        @Id
        private Long individualid;      // PK
        private Integer likedcount;
        private Integer commentcount;

}

