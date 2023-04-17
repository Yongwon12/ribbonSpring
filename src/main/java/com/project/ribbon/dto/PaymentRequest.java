package com.project.ribbon.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String merchantUid;
    private String impUid;
    private PaymentData response;
    private Integer amount;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
