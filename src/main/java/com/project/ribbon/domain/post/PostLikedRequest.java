package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikedRequest {

        private Integer categoryid;
        private Integer userid;

        private Integer inherentid;
        private Long id;

}

