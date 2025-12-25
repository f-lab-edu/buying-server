package org.example.buyingserver.order.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.order.domain.OrderType;
import org.example.buyingserver.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderIdGenerator {

    private static final int MAX_RETRIES = 10;
    private final OrderRepository orderRepository;

    public String generateUnique(OrderType orderType) {
        String orderId;
        int retryCount = 0;

        do {
            orderId = generate(orderType);
            retryCount++;

            if (retryCount >= MAX_RETRIES) {
                log.error("주문번호 생성 실패: 최대 재시도 횟수({}) 초과", MAX_RETRIES);
                throw new RuntimeException("주문번호 생성 실패: 최대 재시도 횟수 초과");
            }

        } while (orderRepository.existsByOrderId(orderId));

        if (retryCount > 1) {
            log.warn("주문번호 생성 재시도: {}번 시도 후 성공, orderId={}", retryCount, orderId);
        }

        return orderId;
    }


    //주문번호 포맷 : TMMDDXXXXX
    //T: Type (S: Subscribe, O: 구매, R: 판매)
    //년도 2자리, 월 2자리, 일 2자리 6자리 영문자 + 숫자는 랜덤

    private String generate(OrderType orderType) {
        String typeCode = orderType.getCode();

        LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyMMdd"));

        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,6)
                .toUpperCase();
        return typeCode + date + uuid;

    }
}