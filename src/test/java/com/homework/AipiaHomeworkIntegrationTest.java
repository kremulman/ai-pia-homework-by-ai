package com.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.MemberDto;
import com.homework.dto.OrderDto;
import com.homework.dto.PaymentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AipiaHomeworkIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입 -> 주문 생성 -> 결제 처리 통합 테스트")
    void full_flow_test() throws Exception {
        // 1. 회원 가입
        MemberDto.Request memberRequest = MemberDto.Request.builder()
                .email("flow@example.com")
                .name("Flow Tester")
                .password("pass")
                .build();

        MvcResult memberResult = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("flow@example.com"))
                .andReturn();

        MemberDto.Response memberResponse = objectMapper.readValue(
                memberResult.getResponse().getContentAsString(), MemberDto.Response.class);
        Long memberId = memberResponse.getId();

        // 2. 주문 생성
        OrderDto.Request orderRequest = OrderDto.Request.builder()
                .memberId(memberId)
                .amount(10000L)
                .build();

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(10000))
                .andReturn();

        OrderDto.Response orderResponse = objectMapper.readValue(
                orderResult.getResponse().getContentAsString(), OrderDto.Response.class);
        Long orderId = orderResponse.getId();

        // 3. 결제 처리
        PaymentDto.Request paymentRequest = PaymentDto.Request.builder()
                .orderId(orderId)
                .amount(10000L)
                .build();

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        // 4. 주문 상태 확인 (조회)
        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
