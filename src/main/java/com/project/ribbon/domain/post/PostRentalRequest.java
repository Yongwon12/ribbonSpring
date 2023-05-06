package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRentalRequest {

        private Long inherentid;
        private Integer price1;
        private Integer price2;
        private Integer price3;
        private Integer price4;
        private Integer price5;
        private Integer price6;
        private Integer price7;
        private Integer price8;
        private Integer price9;
        private Integer price10;
        private String productname1;
        private String productname2;
        private String productname3;
        private String productname4;
        private String productname5;
        private String productname6;
        private String productname7;
        private String productname8;
        private String productname9;
        private String productname10;

        private String rentalimage1;
        private String rentalimage2;
        private String rentalimage3;
        private String rentalimage4;
        private String rentalimage5;
        private String rentalimage6;
        private String rentalimage7;
        private String rentalimage8;
        private String rentalimage9;
        private String rentalimage10;


}

