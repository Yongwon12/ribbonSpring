package com.project.ribbon.dto;

import com.google.api.client.util.DateTime;
import com.project.ribbon.customvaild.DigitLength;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {
    @Id
    private Long paymentid;
    private DateTime paydate;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 4, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;
    private String merchantUid;
    private String impUid;
    private String buyerName;

}
