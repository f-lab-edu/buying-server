package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentNotDoneException extends BusinessException {

    public PaymentNotDoneException() {
        super(PaymentErrorCode.PAYMENT_NOT_DONE);
    }
}
