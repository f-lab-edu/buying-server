package org.example.buyingserver.chat.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class BuyerConflictWithSellerException extends BusinessException {
    public BuyerConflictWithSellerException() {
        super(ChatErrorCode.BUYER_CANNOT_BE_SELLER);
    }
}