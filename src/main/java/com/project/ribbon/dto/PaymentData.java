package com.project.ribbon.dto;

import lombok.Data;

@Data
public class PaymentData {
    private String merchantUid;
    private Integer amount;
    private String status;
    private PaymentData response;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
