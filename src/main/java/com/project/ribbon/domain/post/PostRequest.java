package com.project.ribbon.domain.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

        private Integer id;
        private Long userid;
        private String title;
        private String description;
        private String img;
        private String writedate;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
        private Long boardid;   // PK
        private Integer likedcount;
        private Integer commentcount;

}

