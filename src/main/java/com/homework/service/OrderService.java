package com.homework.service;

import com.homework.domain.Member;
import com.homework.domain.Order;
import com.homework.domain.OrderStatus;
import com.homework.dto.OrderDto;
import com.homework.repository.MemberRepository;
import com.homework.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrderDto.Response createOrder(OrderDto.Request request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(request.getAmount())
                .build();

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    public OrderDto.Response findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return convertToResponse(order);
    }

    private OrderDto.Response convertToResponse(Order order) {
        return OrderDto.Response.builder()
                .id(order.getId())
                .memberId(order.getMember().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
