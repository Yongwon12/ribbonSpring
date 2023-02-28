package com.project.ribbon.domain.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReportUserResponse {
        private Long userid; //PK
        private String nickname;
        private String description;
        private String profileimage;



}
