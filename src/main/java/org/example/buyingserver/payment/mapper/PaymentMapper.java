package org.example.buyingserver.payment.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.order.domain.Order;
import org.example.buyingserver.order.repository.OrderRepository;
import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.domain.Payment;
import org.example.buyingserver.payment.dto.PaymentApproveResponse;
import org.example.buyingserver.payment.repository.PaymentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    //READY상태로 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment saveReadyPayment(Order order, PGProvider provider) {
        Payment payment = paymentRepository.findByOrderId(order.getOrderId())
                .orElseGet(() -> {
                    Payment newPayment = Payment.createPending(order, provider);
                    return paymentRepository.save(newPayment);
                });
        log.info("Payment(READY) 저장 완료: paymentId={}, orderId={}",
                payment.getId(), order.getOrderId());
        return payment;
    }

    //결제 성공 결과 반영
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSuccess(Payment payment, PaymentApproveResponse response, Order order) {
        payment.markSuccess(
                response.paymentKey(),
                response.method(),
                response.approvedAt()
        );
        paymentRepository.save(payment);

        order.markAsPaid();
        orderRepository.save(order);

        log.info("결제 성공 반영 완료: paymentId={}, orderId={}",
                payment.getId(), order.getOrderId());
    }

    //결제 실패 이력 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailure(Payment payment, String failureCode, String failureMessage) {
        payment.markFailed(failureCode, failureMessage);
        paymentRepository.save(payment);
        log.warn("결제 실패 이력 저장: paymentId={}, orderId={}, code={}, message={}",
                payment.getId(), payment.getOrderId(), failureCode, failureMessage);
    }
}