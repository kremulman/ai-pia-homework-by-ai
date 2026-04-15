package com.homework.service;

import com.homework.domain.Member;
import com.homework.dto.MemberDto;
import com.homework.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto.Response join(MemberDto.Request request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();

        Member savedMember = memberRepository.save(member);
        return convertToResponse(savedMember);
    }

    public MemberDto.Response findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return convertToResponse(member);
    }

    private MemberDto.Response convertToResponse(Member member) {
        return MemberDto.Response.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
