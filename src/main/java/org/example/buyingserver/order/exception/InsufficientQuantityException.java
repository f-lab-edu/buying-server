package org.example.buyingserver.order.exception;
import org.example.buyingserver.common.exception.BusinessException;

public class InsufficientQuantityException extends BusinessException{

    public InsufficientQuantityException() {
        super(OrderErrorCode.INSUFFICIENT_QUANTITY);

    }
}
