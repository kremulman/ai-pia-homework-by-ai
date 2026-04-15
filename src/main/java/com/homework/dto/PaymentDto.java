package com.homework.dto;

import com.homework.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class PaymentDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long orderId;
        private Long amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long orderId;
        private LocalDateTime paymentDate;
        private Long amount;
        private PaymentStatus status;
    }
}
