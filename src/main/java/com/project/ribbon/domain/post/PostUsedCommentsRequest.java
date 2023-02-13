package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedCommentsRequest {

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,50}$", message = "댓글은 특수문자를 제외한 최소 2자, 최대 50자입니다.")
        @NotBlank(message="댓글은 필수 입력 값입니다.")
        private String description;
        private Integer userid;
        private String  nickname;
        private Long inherentid;
        private String writedate;
        @Id
        private Long commentsid;
        private String token;

}

