package org.example.buyingserver.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.common.dto.ErrorCode;
import org.example.buyingserver.common.dto.ErrorResponse;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.common.exception.GlobalErrorCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatStompExceptionHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        log.error("[STOMP-ERROR] 메시지 처리 중 예외 발생", ex);

        // 메세지 전달중 발생 에러 추출하기
        Throwable cause = (ex instanceof MessageDeliveryException)
                ? ex.getCause()
                : ex;

        if (cause instanceof BusinessException be) {
            ErrorCode errorCode = be.getErrorCode();
            log.warn("[STOMP-ERROR] : {} - {}",
                    errorCode.getClass().getSimpleName(),
                    errorCode.getMessage());
            return sendErrorMessage(clientMessage, errorCode);
        }

        log.error("[STOMP-ERROR] 예상치 못한 예외: {}", cause.getClass().getName(), cause);
        return sendErrorMessage(clientMessage, GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private Message<byte[]> sendErrorMessage(Message<byte[]> clientMessage, ErrorCode errorCode) {
        try {
            // 원본 메시지의 헤더 정보 가져오기
            StompHeaderAccessor clientAccessor = StompHeaderAccessor.wrap(clientMessage);
            String receiptId = clientAccessor.getReceipt();

            // 에러 메시지 헤더 생성
            StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            errorAccessor.setMessage(errorCode.getMessage());
            errorAccessor.setLeaveMutable(true);

            // 원본 메시지의 receipt-id가 있으면 포함
            if (receiptId != null) {
                errorAccessor.setReceiptId(receiptId);
            }

            ErrorResponse errorResponse = ErrorResponse.fail(errorCode);
            String payload = objectMapper.writeValueAsString(errorResponse);

            log.debug("[STOMP-ERROR] 에러 메시지 전송: {}", payload);

            return MessageBuilder.createMessage(
                    payload.getBytes(StandardCharsets.UTF_8),
                    errorAccessor.getMessageHeaders());

        } catch (Exception e) {
            log.error("[STOMP-ERROR] 에러 메시지 생성 실패", e);
            String fallbackPayload = String.format(
                    "{\"message\":\"%s\",\"code\":%d}",
                    errorCode.getMessage().replace("\"", "\\\""),
                    errorCode.getCode());

            StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            errorAccessor.setMessage(errorCode.getMessage());
            errorAccessor.setLeaveMutable(true);

            return MessageBuilder.createMessage(
                    fallbackPayload.getBytes(StandardCharsets.UTF_8),
                    errorAccessor.getMessageHeaders());
        }
    }

}
