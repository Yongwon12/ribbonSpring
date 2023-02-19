package com.project.ribbon.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Data
@ToString(exclude = {"file"})
@NoArgsConstructor
@AllArgsConstructor
public class PostUserUpdateImageRequest {

        private Long userid;
        private List<MultipartFile> file;

}

