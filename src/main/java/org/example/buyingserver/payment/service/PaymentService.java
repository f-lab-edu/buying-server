package org.example.buyingserver.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.order.domain.Order;
import org.example.buyingserver.order.exception.OrderNotFoundException;
import org.example.buyingserver.order.repository.OrderRepository;
import org.example.buyingserver.payment.client.TossPaymentsClient;
import org.example.buyingserver.payment.config.TossPaymentsWebClientConfig;
import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.domain.Payment;
import org.example.buyingserver.payment.domain.PaymentStatus;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.dto.TossApproveRequest;
import org.example.buyingserver.payment.dto.TossApproveResponse;
import org.example.buyingserver.payment.exception.PaymentAlreadyDoneException;
import org.example.buyingserver.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentsClient tossPaymentsClient;


    @Transactional
    public void approvePayment(PaymentApproveRequest request) {
        //주문범호로 조회
        Order order = orderRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new OrderNotFoundException());

        //중복 결제 방지 체크
        paymentRepository.findByOrderId(request.orderId())
                .ifPresent(payment -> {
                    if (payment.getStatus() == PaymentStatus.DONE) {
                        throw new PaymentAlreadyDoneException();
                    }
                });

        //금액 검증
        //ToDo: 금액 불일치에 대한 예외처리
        if (order.getTotalAmount() != request.amount()) {
            throw new IllegalArgumentException("금액 불일치");
        }

        //PaymentApproveRequest을  TossApproveRequest로 변환
        TossApproveRequest tossRequest = new TossApproveRequest(
                request.orderId(),
                request.amount()
        );
        // paymentKey는 제외 URI 경로로 전달할 예정

        //토스페이먼츠 API 호출
        TossApproveResponse tossResponse = tossPaymentsClient.approvePayment(
                request.paymentKey(),
                tossRequest
        );

        // 5-6. Payment 엔티티 저장
        Payment payment = Payment.createPending(order, PGProvider.TOSS);
        payment.markSuccess(
                tossResponse.paymentKey(),
                tossResponse.method(),
                approvedAt
        );
        paymentRepository.save(payment);

        //Order 상태 변경
        order.markAsPaid();
        orderRepository.save(order);
    }
}
