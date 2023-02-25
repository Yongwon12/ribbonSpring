package com.project.ribbon.controller;


import com.project.ribbon.domain.post.MemberLoginRequestDto;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.service.MemberService;
import com.project.ribbon.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/admin/login")
    public String showLoginForm() {
        return "admin-login";
    }
    @GetMapping("/admin/report")
    public String showReportForm() {
        return "admin-report";
    }
//    @GetMapping("/admin/report")
//    public String userReport(Model model) {
//        List<PostReportUserResponse> userList = postService.findReportUserAllPost();
//        model.addAttribute("userList", userList);
//        return "admin-report";
//    }
@PostMapping( "/admin/login")
public void adminLogin(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) throws IOException {
    String userid = memberLoginRequestDto.getUserid();
    String password = memberLoginRequestDto.getPassword();
    TokenInfo tokenInfo = memberService.login(userid, password);
    if (tokenInfo != null) {
        System.out.println(memberLoginRequestDto.getUserid());
        System.out.println(memberLoginRequestDto.getPassword());
        System.out.println(tokenInfo.getAccessToken());
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/admin/report");
    } else {
        response.sendRedirect("/admin/login");
    }
}

}
