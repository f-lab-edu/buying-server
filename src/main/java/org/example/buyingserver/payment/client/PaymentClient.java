package org.example.buyingserver.payment.client;

import org.example.buyingserver.payment.dto.PaymentApproveRequest;

public interface PaymentClient {
    PaymentApproveRequest approve(PaymentApproveRequest approveRequest);

}
