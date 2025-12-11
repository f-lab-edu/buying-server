package org.example.buyingserver.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 승인 API
     * 프론트엔드에서 토스페이먼츠 successUrl로 리다이렉트된 후 호출한다
     *
     * @param request 결제 승인 요청 (paymentKey, orderId, amount)
     * @return 200 OK (body 없다)
     */
    @PostMapping("/approve")
    public ResponseEntity<Void> approvePayment(
            @RequestBody PaymentApproveRequest request) {
        paymentService.approvePayment(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}