package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReplyRequest {

        private String description;
        private Long userid;
        private String  nickname;
        private Long categoryid;
        private Long inherentid;
        private String writedate;
        private Integer likedcount;
        @Id
        private Long replyid;
        private Long inherentcommentsid;
        private Integer isrecomment;

}

