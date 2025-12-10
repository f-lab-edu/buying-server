package org.example.buyingserver.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//토스페이먼츠 결제 승인 응답

public record TossApproveResponse(
        @JsonProperty("paymentKey")
        String paymentKey,

        @JsonProperty("orderId")
        String orderId,

        @JsonProperty("status")
        String status,

        @JsonProperty("totalAmount")
        Long totalAmount,

        @JsonProperty("method")
        String method,  // "카드", "계좌이체" 등

        @JsonProperty("approvedAt")
        String approvedAt,  // 승인 시간 (ISO 8601 형식)

        @JsonProperty("card")
        Card card  // 카드 결제 시 상세 정보 (null일 수 있음 확인필요)
) {
     //카드 결제 상세 정보
    public record Card(
            @JsonProperty("number")
            String number,  // 카드 번호

            @JsonProperty("installmentPlanMonths")
            Integer installmentPlanMonths  // 할부 개월 수(이것도 저장해야하느낙?)
    ) {}
}