package com.homework.service;

import com.homework.domain.Member;
import com.homework.dto.MemberDto;
import com.homework.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 성공")
    void join_success() {
        // given
        MemberDto.Request request = MemberDto.Request.builder()
                .email("test@example.com")
                .name("Tester")
                .password("password")
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(request.getEmail())
                .name(request.getName())
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberDto.Response response = memberService.join(request);

        // then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getName()).isEqualTo(request.getName());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 이메일 가입 실패")
    void join_fail_duplicate_email() {
        // given
        MemberDto.Request request = MemberDto.Request.builder()
                .email("test@example.com")
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(Member.builder().build()));

        // when & then
        assertThatThrownBy(() -> memberService.join(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");
    }
}
