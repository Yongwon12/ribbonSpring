package com.project.ribbon.dto;

import com.project.ribbon.customvaild.DigitLength;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCancelRequest {
    private String merchantUid;
    private String impUid;
    @NotNull(message = "가격은 필수 입력값입니다.")
    @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.")
    private Integer amount;
    private String taxFree;
    private String vatAmount;
    private String checksum;
    private String reason;
    private String refundHolder;
    private String refundBank;
    private String refundAccount;
    private String refundTel;
    private String paydate;
    private String canceldate;

}
