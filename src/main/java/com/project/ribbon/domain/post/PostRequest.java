package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {
        @Id
        private Long boardid;   // PK
        private Integer id;
        private Long userid;

        private String title;

        private String description;

        private String img;
        private String writedate;

        private String nickname;
        private Integer likedcount;
        private Integer commentcount;

}

