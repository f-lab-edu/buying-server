package org.example.buyingserver.order.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class OrderNotReadyException extends BusinessException {

    public OrderNotReadyException() {
        super(OrderErrorCode.ORDER_NOT_READY);
    }
}