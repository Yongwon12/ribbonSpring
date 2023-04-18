package com.project.ribbon.dto;

import com.google.api.client.util.DateTime;
import com.project.ribbon.customvaild.DigitLength;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentData {
    @Id
    private Long paymentid;
    private String merchant_uid;
    private DateTime paydate;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 4, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;
    private String imp_uid;
    private String buyer_name;
    private String status;
    private PaymentData response;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
