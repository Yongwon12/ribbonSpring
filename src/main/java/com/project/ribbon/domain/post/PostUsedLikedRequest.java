package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedLikedRequest {

        private Integer categoryid;
        private String nickname; // 좋아요 누른 사람 닉네임
        private Long inherentid;
        private String token;
        @Id
        private Long id;
        private String title;
        private String description;
        private Long userid; // 좋아요 눌린 당한 사람 아이디

}

