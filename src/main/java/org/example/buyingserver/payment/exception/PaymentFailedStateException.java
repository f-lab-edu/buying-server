package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentFailedStateException extends BusinessException {

    public PaymentFailedStateException() {
        super(PaymentErrorCode.PAYMENT_FAILED_STATE);
    }
}