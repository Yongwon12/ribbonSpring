package com.project.ribbon.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUsedLikedRequest {

        private Integer categoryid;
        private Integer userid;

        private Integer inherentid;

}

