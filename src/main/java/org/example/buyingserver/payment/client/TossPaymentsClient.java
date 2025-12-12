package org.example.buyingserver.payment.client;

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

    /**
     * 결제 승인 API 호출
     * POST /payments/{paymentKey}/confirm
     *
     * @param paymentKey 토스페이먼츠에서 발급한 결제 키
     * @param request 결제 승인 요청 (orderId, amount)
     * @return TossApproveResponse 결제 승인 응답
     * @throws WebClientResponseException API 호출 실패 시
     */
    public TossApproveResponse approvePayment(TossApproveRequest request) {
        log.info("토스페이먼츠 결제 승인 요청: paymentKey={}, orderId={}, amount={}",
                request.paymentKey(), request.orderId(), request.amount());

        try {
            TossApproveResponse response = tossPaymentsWebClient
                    .post()
                    .uri("/v1/payments/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TossApproveResponse.class)
                    .block();

            log.info("토스페이먼츠 결제 승인 성공: orderId={}, status={}, amount={}",
                    response.orderId(), response.status(), response.totalAmount());

            return response;
        } catch (WebClientResponseException e) {
            log.error("토스페이먼츠 API 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
