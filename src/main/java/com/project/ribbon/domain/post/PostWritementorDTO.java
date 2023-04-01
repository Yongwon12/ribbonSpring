package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.type.DateTime;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
        private String carrer;

        private String titleimage;
        private DateTime writedate;
        @Pattern(regexp = "^\\d{4,7}$", message = "가격은 4~7자여야 합니다.")
        @NotBlank(message = "가격은 필수 입력값입니다.")
        private Integer price;
        @NotBlank(message = "위치는 필수 입력값입니다.")
        private String region;
        @NotBlank(message = "가능시간은 필수 입력값입니다.")
        private String contacttime;
}
