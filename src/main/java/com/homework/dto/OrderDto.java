package com.homework.dto;

import com.homework.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class OrderDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long memberId;
        private Long amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long memberId;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private Long totalAmount;
    }
}
