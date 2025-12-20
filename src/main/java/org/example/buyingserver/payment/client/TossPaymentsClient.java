package org.example.buyingserver.payment.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.dto.PaymentApproveResponse;
import org.example.buyingserver.payment.dto.TossApproveRequest;
import org.example.buyingserver.payment.dto.TossApproveResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient implements PaymentClient {
    private final WebClient tossPaymentsWebClient;

    @Override
    public PaymentApproveResponse approve(PaymentApproveRequest request) {
        log.info("토스페이먼츠 결제 승인 요청: paymentKey={}, orderId={}, amount={}",
                request.paymentKey(), request.orderId(), request.amount());


        //1. PaymentApproveRequest 요청온거를 Toss dto에 맞춰서 변경하기
        try {
            TossApproveRequest tossRequest =  convertToTossRequest(request);
            TossApproveResponse response = callTossPaymentsApi(tossRequest);

            log.info("토스페이먼츠 결제 승인 성공: orderId={}, status={}, amount={}",
                    response.orderId(), response.status(), response.totalAmount());

            return convertToPaymentResult(response);

        } catch (WebClientResponseException e) {
            log.error("토스페이먼츠 API 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
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
