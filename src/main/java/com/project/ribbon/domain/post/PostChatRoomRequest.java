package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostChatRoomRequest {

        @Id
        private Long id;
        private String roomname;

        private Integer myid;

        private Integer yourid;
        private String mynickname;
        private String yournickname;
        private String myprofileimage;
        private String yourprofileimage;


}

