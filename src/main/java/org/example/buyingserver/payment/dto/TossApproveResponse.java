package org.example.buyingserver.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

//토스페이먼츠 결제 승인 응답 DTO
@Getter
public class TossApproveResponse {

    @JsonProperty("paymentKey")
    private String paymentKey;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("totalAmount")
    private Long totalAmount;

    @JsonProperty("method")
    private String method;  // "카드", "계좌이체" 등 저장

    @JsonProperty("approvedAt")
    private String approvedAt;  // 승인 시간

    @JsonProperty("card")
    private Card card;  // 카드 결제 시 상세 정보

    @Getter
    public static class Card {
        @JsonProperty("number")
        private String number;  // 카드 번호 (마스킹됨)

        @JsonProperty("installmentPlanMonths")
        private Integer installmentPlanMonths;  // 할부 개월 수
    }
}