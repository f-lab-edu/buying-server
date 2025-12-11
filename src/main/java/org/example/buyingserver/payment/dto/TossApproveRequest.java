package org.example.buyingserver.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TossApproveRequest(
        @JsonProperty("orderId")
        String orderId,

        @JsonProperty("amount")
        Long amount         // 결제 금액
) {}