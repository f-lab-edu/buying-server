package org.example.buyingserver.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.payment.dto.PaymentApproveRequest;
import org.example.buyingserver.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/approve")
    public ResponseEntity<Void> approvePayment(
            @RequestBody PaymentApproveRequest request) {

        paymentService.approvePayment(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}