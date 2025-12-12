package org.example.buyingserver.payment.dto;

//프론트전송용
public record PaymentApproveRequest(
        String paymentKey,
        String orderId,
        Number amount

) {
}