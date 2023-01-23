package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGroupCommentsRequest {

        private String description;
        private Integer userid;
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String  nickname;
        private Long inherentid;
        private String writedate;
        @Id
        private Long groupcommentsid;

}

