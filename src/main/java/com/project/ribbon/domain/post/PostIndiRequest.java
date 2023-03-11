package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostIndiRequest {

        @NotNull(message = "카테고리는 필수 입력값입니다.")
        private Integer id;
        @Size(min = 2, max = 40, message = "활동시간은 2~40자리여야 합니다.")
        @NotBlank(message="활동시간은 필수 입력 값입니다.")
        private String region;
        @NotBlank(message="날짜는 필수 입력 값입니다.")
        private String meetdate;
        @Size(min = 2, max = 30, message = "제목은 2~30자리여야 합니다.")
        @NotBlank(message="제목은 필수 입력 값입니다.")
        private String title;
        @NotBlank(message="내용은 필수 입력 값입니다.")
        private String description;
        @NotBlank(message="성별은 필수 입력 값입니다.")
        private String gender;
        @NotNull(message="유저아이디는 필수 입력 값입니다.")
        private Long userid;
        @NotBlank(message="작성날짜는 필수 입력 값입니다.")
        private String writedate;
        @NotBlank(message="최대나이는 필수 입력 값입니다.")
        private String maxage;
        @NotBlank(message="최소나이는 필수 입력 값입니다.")
        private String minage;
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
        @Id
        private Long individualid;      // PK
        private Integer likedcount;
        private Integer commentcount;


}

