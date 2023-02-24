package com.project.ribbon.controller;


import com.project.ribbon.domain.post.MemberLoginRequestDto;
import com.project.ribbon.domain.post.PostReportUserResponse;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.service.MemberService;
import com.project.ribbon.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/admin/login")
    public String showLoginForm() {
        return "admin-login";
    }
    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
        String userid = memberLoginRequestDto.getUserid();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            response.addHeader("Authorization", "Bearer " + tokenInfo.getAccessToken());
            return "admin-report"; // 헤더 추가 후 admin-report.html 페이지로 이동
        } else {
            return "admin-login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }


    @GetMapping("/admin/report")
    public String userReport(Model model) {
        List<PostReportUserResponse> userList = postService.findReportUserAllPost();
        model.addAttribute("userList", userList);
        return "admin-report";
    }

}
