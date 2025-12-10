package org.example.buyingserver.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

 //토스페이먼츠 결제 승인 요청 DTO
//POST /payments/{paymentKey}/confirm. ????

public record TossApproveRequest(
        @JsonProperty("orderId")
        String orderId,

        @JsonProperty("amount")
        Long amount         // 결제 금액
) {}