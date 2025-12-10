package org.example.buyingserver.payment.repository;

import org.example.buyingserver.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

     //주문 번호로 Payment 조회
    Optional<Payment> findByOrderId(String orderId);

    //토스페이먼츠 결제 키로 Payment 조회

    Optional<Payment> findByPgPaymentId(String pgPaymentId);
}