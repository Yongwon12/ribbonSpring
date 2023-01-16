package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGroupReplyRequest {

        private String description;
        private String writedate;
        private Long userid;
        private String  nickname;
        private Long inherentid;
        private Integer likedcount;
        @Id
        private Long groupreplyid;
        private Long inherentcommentsid;
        private Integer isrecomment;

}

