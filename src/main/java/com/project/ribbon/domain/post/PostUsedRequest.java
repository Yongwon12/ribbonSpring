package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ribbon.customvaild.DigitLength;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUsedRequest {

        private String id;
        private String region;
        private String detailregion;

        private String title;

        private String description;
        private String shortdescription;
        private String usedimage1;
        private Long userid;
        private String writedate;

        private String nickname;
        @Id
        private Long usedid;      // PK
        private String  usedimage2;
        private String  usedimage3;
        private String  usedimage4;
        private String  usedimage5;
        private Integer  likedcount;
        private Integer  commentcount;
        private String merchantUid;
        private Long inherentid;
        private String storename;
        private Integer numpeople;
        private String opentime;
        private String storetel;
        private String parking;
        private String holiday;
        @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.")
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
        @Size(min = 2, max = 20, message = "상품명은 2~20자리여야 합니다.") @NotBlank(message = "상품명은 필수 입력값입니다.")
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

}

