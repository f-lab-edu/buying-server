package org.example.buyingserver.payment.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class UnsupportedPaymentProviderException extends BusinessException {

    public UnsupportedPaymentProviderException() {
        super(PaymentErrorCode.UNSUPPORTED_PAYMENT_PROVIDER);
    }
}