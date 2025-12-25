package org.example.buyingserver.order.dto;

//토스에 전송해야하는 필수 값들 (백엔드 -> 프론트해서 프론트에서 토스에 전달해줄 값들)
public record OrderCreateResponse(
        String orderId,
        String orderName,
        Long amount
) {}
