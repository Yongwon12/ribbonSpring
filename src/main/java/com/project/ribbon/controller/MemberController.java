package com.project.ribbon.controller;

import com.project.ribbon.domain.post.MemberLoginRequestDto;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String username = memberLoginRequestDto.getUsername();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(username, password);
        return tokenInfo;
    }
    @PostMapping("/test")
    public String test() {
        return "success";
    }

}