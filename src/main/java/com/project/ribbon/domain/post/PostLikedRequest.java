package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostLikedRequest {

        private Integer categoryid;
        private String nickname; // 좋아요 누른 사람 닉네임
        private Long inherentid;
        private String token;
        @Id
        private Long id;
        private String title;
        private String description;
        private Long userid; // 좋아요 눌린 당한 사람 아이디
        private Long myid; // 좋아요 누른 사람 아이디


}

