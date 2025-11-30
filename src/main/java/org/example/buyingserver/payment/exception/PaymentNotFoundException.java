package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException() {
        super(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }
}