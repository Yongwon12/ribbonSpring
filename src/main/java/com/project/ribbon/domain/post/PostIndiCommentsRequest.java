package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIndiCommentsRequest {

        private String description;
        private Integer userid;
        private String  nickname;
        private Long inherentid;
        private String writedate;
        @Id
        private Long individualcommentsid;

}

