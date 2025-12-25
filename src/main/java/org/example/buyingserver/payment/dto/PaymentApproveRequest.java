package org.example.buyingserver.payment.dto;

import org.example.buyingserver.payment.domain.PGProvider;

public record PaymentApproveRequest(
        String paymentKey,
        String orderId,
        Number amount,
        PGProvider pgProvider
) {
}