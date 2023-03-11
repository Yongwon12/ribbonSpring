package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostGroupRequest {

        private Integer id;
        private String region;

        private String title;

        private String description;

        private String line;
        private String peoplenum;
        private String gender;
        private String minage;
        private String titleimage;
        private Long userid;
        private String maxage;
        private String writedate;
        private String peoplenownum;

        private String nickname;
        @Id
        private Long groupid;// PK
        private String once;
        private Integer commentcount;


}

