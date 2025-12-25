package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentNotReadyException extends BusinessException {

    public PaymentNotReadyException() {
        super(PaymentErrorCode.PAYMENT_NOT_READY);
    }
}