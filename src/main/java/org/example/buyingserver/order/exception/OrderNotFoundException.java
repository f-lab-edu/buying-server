package org.example.buyingserver.order.exception;

import org.example.buyingserver.common.exception.BusinessException;

import static org.example.buyingserver.order.exception.OrderErrorCode.ORDER_NOT_FOUND;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException() {
        super(OrderErrorCode.ORDER_NOT_READY);
    }
}