package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedReplyRequest {

        private String description;
        private String writedate;
        private Long userid;
        private String  nickname;
        private Long inherentid;

        @Id
        private Long usedreplyid;
        private Long inherentcommentsid;
        private Integer isrecomment;


}

