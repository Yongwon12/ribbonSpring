package com.project.ribbon.controller;


import com.project.ribbon.domain.post.AdminLoginRequestDto;
import com.project.ribbon.domain.post.PostReportUserDeleteRequest;
import com.project.ribbon.domain.post.PostReportUserResponse;
import com.project.ribbon.domain.post.PostUserRequest;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.service.MemberService;
import com.project.ribbon.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/ribbon/admin/boardlogin")
    public String showBoardLoginForm() {
        return "admin-boardlogin";
    }
    @GetMapping("/ribbon/admin/userlogin")
    public String showUserLoginForm() {
        return "admin-userlogin";
    }
    @GetMapping("/ribbon/admin/commentslogin")
    public String showcommentsLoginForm() {
        return "admin-commentslogin";
    }
    @GetMapping("/ribbon/admin/report")
    public String showReportForm() {
        return "admin-report";
    }
    @GetMapping("/ribbon/admin/reportboard")
    public String showReportBoardForm() {
        return "admin-reportboard";
    }
    @GetMapping("/ribbon/admin/reportcomments")
    public String showReportCommentsForm() {
        return "admin-reportcomments";
    }

    @GetMapping("/ribbon/admin/reportuser")
    public String userReport(Model model) {
        List<PostReportUserResponse> userList = postService.findReportUserAllPost();
        model.addAttribute("userList", userList);
        return "admin-reportuser";
    }
    // 신고 유저 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportuserdelete")
    public String userReportDelete(@RequestBody PostUserRequest params) {
        postService.deleteUserRolesPost(params);
        postService.deleteUserPost(params);
        postService.deleteUserReportPost(params);
        return "admin-reportuser";
    }

@PostMapping("/ribbon/admin/post/boardlogin")
public void adminBoardLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
    String userid = adminLoginRequestDto.getUserid();
    String password = adminLoginRequestDto.getPassword();
    if (userid.equals("2") && password.equals("G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A")) {
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.219.161:8000/ribbon/admin/reportboard", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/login");
        }
    } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
    @PostMapping("/ribbon/admin/post/userlogin")
    public void adminUserLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        if (userid.equals("2") && password.equals("G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A")) {
            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange("http://192.168.219.161:8000/ribbon/admin/reportuser", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/login");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    @PostMapping("/ribbon/admin/post/commentslogin")
    public void adminCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        if (userid.equals("2") && password.equals("G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A")) {
            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange("http://192.168.219.161:8000/ribbon/admin/reportcomments", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/login");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
