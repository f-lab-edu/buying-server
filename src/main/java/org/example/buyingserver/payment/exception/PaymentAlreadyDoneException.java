package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentAlreadyDoneException extends BusinessException {

    public PaymentAlreadyDoneException() {
        super(PaymentErrorCode.PAYMENT_ALREADY_DONE);
    }
}