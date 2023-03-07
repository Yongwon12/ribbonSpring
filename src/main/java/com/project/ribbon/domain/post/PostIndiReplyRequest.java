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
public class PostIndiReplyRequest {

        private String description;
        private String writedate;
        private Long userid;
        private String  nickname;
        private Long inherentid;
        @Id
        private Long individualreplyid;
        private Long inherentcommentsid;
        private Integer isrecomment;


}

