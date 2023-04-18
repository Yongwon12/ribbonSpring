package com.project.ribbon.dto;

import com.project.ribbon.customvaild.DigitLength;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentResponse {

    @Id
    private Long paymentid;
    private String merchantUid;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 4, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;
    private String impUid;
    private String buyerName;
    private String status;
    private PaymentData response;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
