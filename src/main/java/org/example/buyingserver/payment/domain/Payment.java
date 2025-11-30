package org.example.buyingserver.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.buyingserver.order.domain.Order;
import org.example.buyingserver.payment.exception.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String pgPaymentId;

    //결제사 이름
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PGProvider pgProvider;

    //주문 번호 (우리 ORDER.orderId)
    @Column(nullable = false, length = 64)
    private String orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pk_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private long totalAmount;

    //결제 수단
    @Column(length = 50)
    private String method;

    //PG 원본 응답 저장
    @Lob
    private String rawDataJson;

    //결제 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;

    private String failureCode;
    private String failureMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private Payment(String pgPaymentId,
                    PGProvider pgProvider,
                    String orderId,
                    Order order,
                    long totalAmount,
                    String method,
                    PaymentStatus status,
                    LocalDateTime requestedAt,
                    LocalDateTime approvedAt,
                    String rawDataJson,
                    String failureCode,
                    String failureMessage) {

        this.pgPaymentId = pgPaymentId;
        this.pgProvider = pgProvider;
        this.orderId = orderId;
        this.order = order;
        this.totalAmount = totalAmount;
        this.method = method;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.rawDataJson = rawDataJson;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
    }

    //결제 요청 직전
    public static Payment createPending(Order order, PGProvider pgProvider) {
        return Payment.builder()
                .pgProvider(pgProvider)
                .order(order)
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .status(PaymentStatus.READY)
                .build();
    }

    // 결제 승인 완료
    public void markSuccess(String pgPaymentId,
                            String method,
                            LocalDateTime approvedAt,
                            String rawDataJson) {

        if (this.status != PaymentStatus.READY) {
            throw new PaymentNotReadyException();
        }

        this.pgPaymentId = pgPaymentId;
        this.method = method;
        this.approvedAt = approvedAt;
        this.rawDataJson = rawDataJson;
        this.status = PaymentStatus.DONE;
    }

    //결제 취소
    public void markCanceled() {
        if (this.status != PaymentStatus.DONE) {
            throw new PaymentNotDoneException();
        }
        this.status = PaymentStatus.CANCELED;
    }

    // 결제 실패
    public void markFailed(String code, String message) {
        if (this.status == PaymentStatus.DONE) {
            throw new PaymentAlreadyDoneException();
        }

        this.status = PaymentStatus.FAILED;
        this.failureCode = code;
        this.failureMessage = message;
    }
}