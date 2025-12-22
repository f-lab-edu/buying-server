package org.example.buyingserver.payment.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.order.domain.Order;
import org.example.buyingserver.order.exception.OrderNotFoundException;
import org.example.buyingserver.order.repository.OrderRepository;
import org.example.buyingserver.payment.client.PaymentClient;
import org.example.buyingserver.payment.client.PaymentClientRouter;
import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.domain.Payment;
import org.example.buyingserver.payment.domain.PaymentStatus;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.dto.PaymentApproveResponse;
import org.example.buyingserver.payment.exception.PaymentAlreadyDoneException;
import org.example.buyingserver.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentClientRouter paymentClientRouter;

    @Transactional
    public void approvePayment(PaymentApproveRequest request) {
        log.info("결제 승인 요청: paymentKey={}, orderId={}, amount={}, provider={}",
                request.paymentKey(), request.orderId(), request.amount(), request.pgProvider());

        PGProvider provider = request.pgProvider();

        //주문 번호로 조회
        Order order = validateOrder(request);

        //중복 결제 방지 체크
        validateDuplicatePayment(request.orderId());

        //금액 검증
        validateAmount(order.getTotalAmount(), request.amount().longValue());

        //라우터에게 해당 결제방식을보고 클라이언트 달라고 요청하고 그에 맞는 클래스로 호출
        PaymentClient paymentClient = paymentClientRouter.route(provider);
        PaymentApproveResponse approveResponse = paymentClient.approve(request);

        Payment payment = paymentRepository.findByOrderId(request.orderId())
                .orElseGet(() -> Payment.createPending(order, provider));

            //결제 승인 완료 처리
            payment.markSuccess(
                    approveResponse.paymentKey(),
                    approveResponse.method(),
                    approveResponse.approvedAt()
            );

            paymentRepository.save(payment);
            log.info("Payment 저장 완료: paymentId={}, orderId={}", payment.getId(), request.orderId());

            //Order 상태 변경
            order.markAsPaid();
            orderRepository.save(order);

            log.info("결제 승인 완료: orderId={}, paymentId={}, amount={}",
                    request.orderId(), payment.getId(), request.amount());
        }

    private Order validateOrder(PaymentApproveRequest request) {
        Order order = orderRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> {
                    log.error("주문을 찾을 수 없음: orderId={}", request.orderId());
                    return new OrderNotFoundException();
                });

        return order;
    }

    private void validateDuplicatePayment(String orderId) {
        paymentRepository.findByOrderId(orderId)
                .ifPresent(payment -> {
                    if (payment.getStatus() == PaymentStatus.DONE) {
                        log.error("이미 결제가 완료된 주문: orderId={}, paymentId={}",
                                orderId, payment.getId());
                        throw new PaymentAlreadyDoneException();
                    }
                });
    }

    private void validateAmount(long amount, long totalAmount) {
        if (amount != totalAmount)  {
            log.error("금액 불일치: 주문 금액={}, 요청 금액={}", amount, totalAmount);
            //ToDo: 예외처리클래스 생성해야함
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }
    }
}