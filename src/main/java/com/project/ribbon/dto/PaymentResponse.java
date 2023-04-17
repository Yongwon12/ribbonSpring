package com.project.ribbon.dto;

import lombok.Data;

@Data
public class PaymentResponse {

    private PaymentData response;

    public PaymentData getResponse() {
        return response;
    }

    public void setResponse(PaymentData response) {
        this.response = response;
    }
}
