package org.example.buyingserver.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_NOT_FOUND(404, 6001, "결제 내역을 찾을 수 없습니다."),
    PAYMENT_NOT_READY(400, 6002, "READY 상태에서만 결제를 승인할 수 있습니다."),
    PAYMENT_NOT_DONE(400, 6003, "DONE 상태에서만 결제를 취소할 수 있습니다."),
    PAYMENT_ALREADY_DONE(400, 6004, "이미 결제가 완료된 상태입니다."),
    PAYMENT_FAILED_STATE(400, 6005, "결제를 실패 상태로 변경할 수 없습니다."),
    UNSUPPORTED_PAYMENT_PROVIDER(400, 6006, "지원하지 않는 결제 방식입니다."),
    PAYMENT_AMOUNT_MISMATCH(400, 6007, "결제 금액이 주문 금액과 일치하지 않습니다.");

    private final int status;
    private final int code;
    private final String message;
}