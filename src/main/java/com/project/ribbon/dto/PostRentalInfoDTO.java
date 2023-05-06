package com.project.ribbon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRentalInfoDTO {

        @Id
        private Long id; //PK
        @Size(min = 3,max = 7,message = "가격은 4~7자리")
        @NotBlank(message = "가격은 필수 입력값입니다.")
        private Integer price;
        @NotBlank(message = "유저아이디 필수 입력값입니다.")
        private Long userid;
        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String username;
        private String merchantUid;
        @NonNull
        private Long inherentid;
        @NotBlank
        private String paydate;
        private String title;
        private String impUid;
        private String amount;
        private String rentaltime;
        private String productname;


}
