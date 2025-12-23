package org.example.buyingserver.payment.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.dto.PaymentApproveResponse;
import org.example.buyingserver.payment.dto.TossApproveRequest;
import org.example.buyingserver.payment.dto.TossApproveResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient implements PaymentClient {
    private final WebClient tossPaymentsWebClient;

    @Override
    public PGProvider getProvider() {
        return PGProvider.TOSS;
    }

    @Override
    public PaymentApproveResponse approve(PaymentApproveRequest request) {
        log.info("결제 승인 요청: paymentKey={}, orderId={}, amount={}",
                request.paymentKey(), request.orderId(), request.amount());
        try {
            TossApproveResponse response = callTossPaymentsApi(convertToTossRequest(request));

            log.info("결제 승인 성공: orderId={}, status={}, amount={}",
                    response.orderId(), response.status(), response.totalAmount());

            return convertToPaymentResult(response);

        } catch (WebClientResponseException e) {
            log.error("API 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    @Override
    public void cancel(String paymentKey, String cancelReason) {
        //결제가 성공했는데 DB에 업데이트 못햇을 경우 Toss에 API cancel 요청
        log.info("Toss 결제 취소 요청 시작: paymentKey={}, 사유={}", paymentKey, cancelReason);
        try {
            tossPaymentsWebClient.post()
                    .uri("/v1/payments/{paymentKey}/cancel", paymentKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("cancelReason", cancelReason))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info("Toss 결제 취소 성공: paymentKey={}", paymentKey);
        } catch (WebClientResponseException e) {
            log.error("Toss 결제 취소 API 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("결제 취소 API 호출 실패 - 수동 확인 필요", e);
        } catch (Exception e) {
            log.error("Toss 결제 취소 중 알 수 없는 에러 발생: {}", e.getMessage());
            throw e;
        }
    }

    private TossApproveRequest convertToTossRequest(PaymentApproveRequest request) {
        return new TossApproveRequest(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );
    }

    private TossApproveResponse callTossPaymentsApi(TossApproveRequest request) {
        return tossPaymentsWebClient
                .post()
                .uri("/v1/payments/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TossApproveResponse.class)
                .block();
    }

    private PaymentApproveResponse convertToPaymentResult(TossApproveResponse response) {

        return new PaymentApproveResponse(
                response.paymentKey(),
                response.orderId(),
                response.totalAmount(),
                response.method(),
                response.approvedAt()
        );
    }
}
