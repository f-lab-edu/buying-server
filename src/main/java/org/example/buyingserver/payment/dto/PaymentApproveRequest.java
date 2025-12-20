package org.example.buyingserver.payment.dto;

public record PaymentApproveRequest(
        String paymentKey,
        String orderId,
        Number amount

) {
}