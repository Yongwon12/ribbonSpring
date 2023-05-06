package com.project.ribbon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ribbon.customvaild.DigitLength;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {
    @Id
    private Long paymentid;
    private String paydate;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;
    private String merchantUid;
    private String impUid;
    private String buyerName;
    private Long userid;
    private Long inherentid;
    private String account;
    private String bank;
    private String rentaltime;
    private String productname;

}
