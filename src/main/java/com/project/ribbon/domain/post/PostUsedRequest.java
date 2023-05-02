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
public class PostUsedRequest {

        private Integer id;
        private String region;

        private String title;

        private String description;
        private String usedimage1;

        private Integer price;
        private Long userid;
        private String writedate;

        private String nickname;
        @Id
        private Long usedid;      // PK
        private String  usedimage2;
        private String  usedimage3;
        private String  usedimage4;
        private String  usedimage5;
        private Integer  likedcount;
        private Integer  commentcount;
        private String merchantUid;
        private Long inherentid;
        private String username;


}

