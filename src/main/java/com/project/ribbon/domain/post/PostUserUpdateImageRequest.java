package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserUpdateImageRequest {

        //private Long userid;

        private String userid;
        private String filepath;

}

