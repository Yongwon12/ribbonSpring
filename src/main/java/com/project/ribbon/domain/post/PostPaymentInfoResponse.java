package com.project.ribbon.domain.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostPaymentInfoResponse {

    private Long paymentid;
    private String  paydate;
    private Integer amount;
    private String username;
    private Long userid;
    private String merchantUid;
    private String bank;
    private String account;


}