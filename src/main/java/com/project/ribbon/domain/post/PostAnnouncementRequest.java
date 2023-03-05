package com.project.ribbon.domain.post;

import com.google.type.DateTime;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAnnouncementRequest {
        @Id
        private Long id;
        private String title;   // PK
        private String content;
        private String writedate;

}

