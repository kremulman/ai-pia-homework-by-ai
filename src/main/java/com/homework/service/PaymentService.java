package com.homework.service;

import com.homework.domain.Order;
import com.homework.domain.OrderStatus;
import com.homework.domain.Payment;
import com.homework.domain.PaymentStatus;
import com.homework.dto.PaymentDto;
import com.homework.repository.OrderRepository;
import com.homework.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentDto.Response processPayment(PaymentDto.Request request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getTotalAmount().equals(request.getAmount())) {
            throw new IllegalArgumentException("Payment amount does not match order amount");
        }

        Payment payment = Payment.builder()
                .order(order)
                .paymentDate(LocalDateTime.now())
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .build();

        order.setStatus(OrderStatus.COMPLETED);
        
        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    public PaymentDto.Response findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return convertToResponse(payment);
    }

    private PaymentDto.Response convertToResponse(Payment payment) {
        return PaymentDto.Response.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }
}
