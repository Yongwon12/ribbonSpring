package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostWritementorDTO {

        @Id
        private Long id; //PK
        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;
        @NotBlank(message = "종목은 필수 입력값입니다.")
        private String category;
        private String shortcontent;
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String description;
        @NotBlank(message = "커리어는 필수 입력값입니다.")
        private String career;

        private String titleimage;
        private String writedate;
//        @Pattern(regexp = "^\\d{4,7}$", message = "가격은 4~7자여야 합니다.")
        @Size(min = 4,max = 7,message = "가격은 4~7자리")
        @NotBlank(message = "가격은 필수 입력값입니다.")
        private Integer price;
        @NotBlank(message = "유저아이디 필수 입력값입니다.")
        private Long userid;
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;
        @NotBlank(message = "위치는 필수 입력값입니다.")
        private String region;
        @NotBlank(message = "가능시간은 필수 입력값입니다.")
        private String contacttime;
        private String profileimage;
        private Long inherentid;
        private String review;
        private Integer appraisal;
}
