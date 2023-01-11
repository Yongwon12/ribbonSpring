package com.project.ribbon.domain.post;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

        private Long id;             // PK
        private String title;        // 제목
        private String content;      // 내용
        private String writer;       // 작성자
        private Integer cnt;

}

