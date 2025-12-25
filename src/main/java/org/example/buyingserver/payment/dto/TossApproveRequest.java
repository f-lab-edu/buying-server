package org.example.buyingserver.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TossApproveRequest(
        @JsonProperty("paymentKey")
        String paymentKey,

        @JsonProperty("orderId")
        String orderId,

        @JsonProperty("amount")
        Number amount
) {}