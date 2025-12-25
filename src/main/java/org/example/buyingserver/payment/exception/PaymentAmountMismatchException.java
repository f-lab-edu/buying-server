package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PaymentAmountMismatchException extends BusinessException {

    public PaymentAmountMismatchException() {
        super(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH);
    }
}