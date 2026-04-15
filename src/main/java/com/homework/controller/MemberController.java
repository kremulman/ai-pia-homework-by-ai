package com.homework.controller;

import com.homework.dto.MemberDto;
import com.homework.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDto.Response> join(@RequestBody MemberDto.Request request) {
        return ResponseEntity.ok(memberService.join(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }
}
