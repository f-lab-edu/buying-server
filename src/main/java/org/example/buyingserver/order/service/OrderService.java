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
    private final OrderIdGenerator orderIdGenerator;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request, Member member) {
        Post post = postRepository.findByIdWithLock(request.postId()).orElseThrow(() -> new PostNotFoundException());

        int requestedQuantity = request.quantity();
        int postAmount = post.getPrice();
        long totalAmount = (long)requestedQuantity * postAmount;
        post.descreaseQuantity(requestedQuantity);

        String orderId = orderIdGenerator.generateUnique(OrderType.ORDER);

        Order order = Order.create(
                member,
                orderId,
                post.getId(),
                post.getTitle(),
                post.getTitle(),
                requestedQuantity,
                totalAmount
        );

        orderRepository.save(order);
        log.info("주문 생성 완료: orderId={}, buyerId={}, postId={}, amount={}",
                orderId, member.getId(), post.getId(), totalAmount);

        return new OrderCreateResponse(
                orderId,
                post.getTitle(),
                totalAmount
        );
    }
}