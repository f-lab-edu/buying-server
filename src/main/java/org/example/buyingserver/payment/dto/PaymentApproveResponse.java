package org.example.buyingserver.payment.dto;

import java.time.LocalDateTime;

public record PaymentApproveResponse(
        String paymentKey,
        String orderId,
        Long totalAmount,
        String method,
        LocalDateTime approvedAt
) {
}