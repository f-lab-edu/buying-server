package org.example.buyingserver.order.dto;


//프론트에서 주문이 들어왔을때
public record OrderCreateRequest(
        Long postId,      // 상품
        Integer quantity  // 주문 수량
) {}
