package com.homework.controller;

import com.homework.dto.PaymentDto;
import com.homework.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto.Response> processPayment(@RequestBody PaymentDto.Request request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }
}
