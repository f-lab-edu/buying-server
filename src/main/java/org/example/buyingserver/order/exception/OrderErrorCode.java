package org.example.buyingserver.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(404, 5001, "주문을 찾을 수 없습니다."),
    ORDER_NOT_READY(400, 5002, "READY 상태인 주문만 결제할 수 있습니다."),
    ORDER_ALREADY_CANCELED(400, 5003, "이미 취소된 주문입니다."),
    ORDER_INVALID_STATUS(400, 5004, "유효하지 않은 주문 상태입니다.");

    private final int status;
    private final int code;
    private final String message;
}