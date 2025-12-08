package org.example.buyingserver.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.order.domain.Order;
import org.example.buyingserver.order.domain.OrderType;
import org.example.buyingserver.order.dto.OrderCreateRequest;
import org.example.buyingserver.order.dto.OrderCreateResponse;
import org.example.buyingserver.order.exception.InsufficientQuantityException;
import org.example.buyingserver.order.repository.OrderRepository;
import org.example.buyingserver.order.util.OrderIdGenerator;
import org.example.buyingserver.post.domain.Post;
import org.example.buyingserver.post.exception.PostNotFoundException;
import org.example.buyingserver.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PostRepository postRepository;
    private final OrderIdGenerator orderIdGenerator;  // 주입받기



    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request, Member member) {
        //1.상품 조회 : postId로 상품 정보 조회 없을 경우 예외 처리해주기
        Post post = postRepository.findById(request.postId()).orElseThrow(() -> new PostNotFoundException());

        int requestedQuantity = request.quantity();
        int postedQuantity = post.getQuantity();
        int postAmount = post.getPrice();

        //2. 수량 검증
        if(postedQuantity < requestedQuantity) {
            throw new InsufficientQuantityException();
        }
        //3. 금액 계산하기 수량과 단가
        long totalAmount = (long)requestedQuantity * postAmount;

        //4. 주문 번호 생성하기
        String orderId = orderIdGenerator.generateUnique(OrderType.ORDER);

        //5. Order 엔티티 생성하여 주문 데이터를 생성
        Order order = Order.create(
                member,
                orderId,
                post.getId(),
                post.getTitle(),
                post.getTitle(),
                requestedQuantity,
                totalAmount
        );

        //6. 디비에 저장하고
        orderRepository.save(order);
        log.info("주문 생성 완료: orderId={}, buyerId={}, postId={}, amount={}",
                orderId, member.getId(), post.getId(), totalAmount);

        //7. 응답 dto로 보내는데 토스 필수값만 넣어서 전달
        return new OrderCreateResponse(
                orderId,
                post.getTitle(),
                totalAmount
        );
    }
}
