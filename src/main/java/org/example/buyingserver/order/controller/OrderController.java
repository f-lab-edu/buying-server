package org.example.buyingserver.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.MemberDetails;
import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.common.dto.ResponseCodeAndMessage;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.order.dto.OrderCreateRequest;
import org.example.buyingserver.order.dto.OrderCreateResponse;
import org.example.buyingserver.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    //주문이 들어왔을 때 최초 받는 api , 주문id 생성 및 수량 확인 토큰 도 받기
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal MemberDetails memberDetails) {
        Member  buyer = memberDetails.getMember();
        OrderCreateResponse response = orderService.createOrder(request, buyer);
        return ResponseEntity.ok(response);
    }
}
