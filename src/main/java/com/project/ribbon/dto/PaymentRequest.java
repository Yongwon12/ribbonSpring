package com.project.ribbon.dto;

import com.project.ribbon.customvaild.DigitLength;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    private String merchantUid;
    private String impUid;
    private PaymentData response;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 4, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
