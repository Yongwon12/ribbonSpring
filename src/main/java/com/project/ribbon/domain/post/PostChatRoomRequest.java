package com.project.ribbon.domain.post;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostChatRoomRequest {

        @Id
        private Long id;
        private String roomid;
        private String roomname;

        private Long myid;

        private Long yourid;

        private String createdate;

}

