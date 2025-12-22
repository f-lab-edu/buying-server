package org.example.buyingserver.payment.client;

import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.dto.PaymentApproveResponse;

public interface PaymentClient {
    PaymentApproveResponse approve(PaymentApproveRequest request);

    PGProvider getProvider();

    void cancel(String paymentKey, String cancelReason);
}