package org.example.buyingserver.payment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.payment.dto.TossApproveRequest;
import org.example.buyingserver.payment.dto.TossApproveResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final WebClient tossPaymentsWebClient;

    public TossApproveResponse approvePayment(String paymentKey, TossApproveRequest request) {
        log.info("토스페이먼츠 결제 승인 요청 시작: paymentKey={}, orderId={}, amount={}",
                paymentKey, request.orderId(), request.amount());

        try {
            // WebClient를 사용하여 비동기 호출 후 동기적으로 대기
            TossApproveResponse response = tossPaymentsWebClient
                    .post()

                    // URI 설정 (경로 변수 사용)
                    // /v1/payments/{paymentKey}/confirm
                    // paymentKey가 실제 값으로 치환됨
                    .uri("/v1/payments/{paymentKey}/confirm", paymentKey)

                    // Content-Type 헤더 설정
                    .contentType(MediaType.APPLICATION_JSON)

                    // Request Body 설정
                    // TossApproveRequest를 JSON으로 자동 변환하여 body에 넣음
                    .bodyValue(request)

                    // 응답 가져오기
                    .retrieve()

                    // 응답을 TossApproveResponse로 변환
                    // JSON 응답을 자동으로 TossApproveResponse 변환
                    .bodyToMono(TossApproveResponse.class)

                    // 비동기 호출을 동기적으로 대기 (결과를 기다림)
                    // block()을 사용하여 동기적으로 처리
                    // (서비스 레이어에서 사용하므로 동기 처리가 편리함)
                    .block();

            log.info("토스페이먼츠 결제 승인 성공: orderId={}, status={}, amount={}",
                    response.orderId(), response.status(), response.totalAmount());

            return response;

        } catch (WebClientResponseException e) {
            // 토스페이먼츠 API 호출 실패 시 예외 처리
            log.error("토스페이먼츠 API 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;  // 예외를 상위로 전파 (PaymentService에서 처리)
        }
    }
}