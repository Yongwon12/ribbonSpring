package com.project.ribbon.domain.post;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

        private Integer id;
        private Long userid;
        private String title;
        private String description;
        private String img;
        private String writedate;
        private String profileimage;
        private String nickname;
        private Long boardid;   // PK
        private Integer likedcount;
        private Integer commentcount;

}

