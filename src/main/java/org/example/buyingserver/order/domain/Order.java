package org.example.buyingserver.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;

    //post가 수정될 것을 고려하여 id만 복사하여 가져옴
    @Column(nullable = false)
    private Long productId;

    /** 주문 시점 상품명 */
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private int quantity;

    /** 결제 총 금액 */
    @Column(nullable = false)
    private long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    private Order(String orderId,
                  Member buyer,
                  Long productId,
                  String productName,
                  String orderName,
                  int quantity,
                  long totalAmount,
                  OrderStatus status) {

        this.orderId = orderId;
        this.buyer = buyer;
        this.productId = productId;
        this.productName = productName;
        this.orderName = orderName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order create(Member buyer,
                               String orderId,
                               Long productId,
                               String productName,
                               String orderName,
                               int quantity,
                               long totalAmount) {

        return Order.builder()
                .orderId(orderId)
                .buyer(buyer)
                .productId(productId)
                .productName(productName)
                .orderName(orderName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.READY)
                .build();
    }


    public void markPaid() {
        if (status != OrderStatus.READY)
            throw new IllegalStateException("READY 상태만 결제 완료 가능");
        this.status = OrderStatus.PAID;
    }

    public void markCanceled() {
        if (status == OrderStatus.CANCELED)
            throw new IllegalStateException("이미 취소된 주문입니다.");
        this.status = OrderStatus.CANCELED;
    }

    public void markFailed() {
        if (status == OrderStatus.PAID)
            throw new IllegalStateException("결제완료된 주문은 실패로 변경 불가");
        this.status = OrderStatus.FAILED;
    }
}

