package com.homework.controller;

import com.homework.dto.OrderDto;
import com.homework.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto.Response> createOrder(@RequestBody OrderDto.Request request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }
}
