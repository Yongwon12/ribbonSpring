package com.project.ribbon.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIndividualLikedRequest {

        private Integer categoryid;
        private Integer userid;

        private Integer inherentid;
        private String token;
        private String nickname;
        private String type;

}

