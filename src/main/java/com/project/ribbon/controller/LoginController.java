package com.project.ribbon.controller;


import com.project.ribbon.domain.post.*;
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
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;
    // 커뮤니티 글 신고 로그인 폼
    @GetMapping("/ribbon/admin/boardlogin")
    public String showBoardLoginForm() {
        return "admin-boardlogin";
    }
    // 개인 글 신고 로그인 폼
    @GetMapping("/ribbon/admin/individuallogin")
    public String showIndividualLoginForm() {
        return "admin-individuallogin";
    }
    // 단체 글 신고 로그인 폼
    @GetMapping("/ribbon/admin/grouplogin")
    public String showGroupLoginForm() {
        return "admin-grouplogin";
    }
    // 중고 글 신고 로그인 폼
    @GetMapping("/ribbon/admin/usedlogin")
    public String showUsedLoginForm() {
        return "admin-usedlogin";
    }
    // 유저 신고 로그인 폼
    @GetMapping("/ribbon/admin/userlogin")
    public String showUserLoginForm() {
        return "admin-userlogin";
    }
    // 커뮤니티 댓글 신고 로그인 폼
    @GetMapping("/ribbon/admin/commentslogin")
    public String showCommentsLoginForm() {
        return "admin-commentslogin";
    }
    // 개인 댓글 신고 로그인 폼
    @GetMapping("/ribbon/admin/individualcommentslogin")
    public String showIndividualCommentsLoginForm() {
        return "admin-individualcommentslogin";
    }
    // 단체 댓글 신고 로그인 폼
    @GetMapping("/ribbon/admin/groupcommentslogin")
    public String showGroupCommentsLoginForm() {
        return "admin-groupcommentslogin";
    }
    // 중고 댓글 신고 로그인 폼
    @GetMapping("/ribbon/admin/usedcommentslogin")
    public String showUsedCommentsLoginForm() {
        return "admin-usedcommentslogin";
    }
    @GetMapping("/ribbon/admin/report")
    public String showReportForm() {
        return "admin-report";
    }
    // 신고 유저 정보 조회
    @GetMapping("/ribbon/admin/reportuser")
    public String userReport(Model model) {
        List<PostReportUserResponse> userList = postService.findReportUserAllPost();
        model.addAttribute("userList", userList);
        return "admin-reportpostdeleteuser";
    }
    // 신고 커뮤니티글 정보 조회
    @GetMapping("/ribbon/admin/reportboard")
    public String boardReport(Model model) {
        List<PostReportBoardResponse> boardList = postService.findReportBoardAllPost();
        model.addAttribute("boardList", boardList);
        return "admin-reportpostdeleteboard";
    }
    // 신고 개인글 정보 조회
    @GetMapping("/ribbon/admin/reportindividual")
    public String individualReport(Model model) {
        List<PostReportIndividualResponse> individualList = postService.findReportIndividualAllPost();
        model.addAttribute("individualList", individualList);
        return "admin-reportpostdeleteindividual";
    }
    // 신고 단체글 정보 조회
    @GetMapping("/ribbon/admin/reportgroup")
    public String groupReport(Model model) {
        List<PostReportGroupResponse> groupList = postService.findReportGroupAllPost();
        model.addAttribute("groupList", groupList);
        return "admin-reportpostdeletegroup";
    }
    // 신고 중고글 정보 조회
    @GetMapping("/ribbon/admin/reportused")
    public String usedReport(Model model) {
        List<PostReportUsedResponse> usedList = postService.findReportUsedAllPost();
        model.addAttribute("usedList", usedList);
        return "admin-reportpostdeleteused";
    }
    // 신고 커뮤니티 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportcomments")
    public String commentsReport(Model model) {
        List<PostReportCommentsResponse> commentsList = postService.findReportCommentsAllPost();
        model.addAttribute("commentsList", commentsList);
        return "admin-reportcomments";
    }
    // 신고 개인 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportindividualcomments")
    public String individualcommentsReport(Model model) {
        List<PostReportCommentsResponse> individualcommentsList = postService.findReportIndividualCommentsAllPost();
        model.addAttribute("individualcommentsList", individualcommentsList);
        return "admin-reportindividualcomments";
    }
    // 신고 단체 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportgroupcomments")
    public String groupcommentsReport(Model model) {
        List<PostReportCommentsResponse> groupcommentsList = postService.findReportGroupCommentsAllPost();
        model.addAttribute("groupcommentsList", groupcommentsList);
        return "admin-reportgroupcomments";
    }
    // 신고 중고 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportusedcomments")
    public String usedcommentsReport(Model model) {
        List<PostReportCommentsResponse> usedcommentsList = postService.findReportUsedCommentsAllPost();
        model.addAttribute("usedcommentsList", usedcommentsList);
        return "admin-reportusedcomments";
    }
    // 신고 유저 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportuserdelete")
    public String userReportDelete(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> user:params) {
            String userId = user.get("userid");
            postService.deleteReportUserRolesPost(userId);
            postService.deleteReportUserPost(userId);
            postService.deleteUserReportPost(userId);
        }
        return "admin-reportpostdeleteuser";
    }
    // 관리자페이지 신고 커뮤니티글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportboarddelete")
    public String boardReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> board : params) {
            String boardId = board.get("boardid");
            postService.deleteBoardWriteReportPost(boardId);
            postService.deleteBoardReportPost(boardId);
        }
        return "admin-reportpostdeleteboard";
    }
    // 관리자페이지 신고 개인글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportindividualdelete")
    public String individualReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> individual : params) {
            String individualId = individual.get("individualid");
            postService.deleteIndividualWriteReportPost(individualId);
            postService.deleteIndividualReportPost(individualId);
        }
        return "admin-reportpostdeleteindividual";
    }
    // 관리자페이지 신고 단체글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportgroupdelete")
    public String groupReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> group : params) {
            String groupId = group.get("groupid");
            postService.deleteGroupWriteReportPost(groupId);
            postService.deleteGroupReportPost(groupId);
        }
        return "admin-reportpostdeletegroup";
    }
    // 관리자페이지 신고 중고글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportuseddelete")
    public String usedReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> used : params) {
            String usedId = used.get("usedid");
            postService.deleteUsedWriteReportPost(usedId);
            postService.deleteUsedReportPost(usedId);
        }
        return "admin-reportpostdeleteused";
    }

    // 관리자페이지 신고 커뮤니티 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportcommentsdelete")
    public String commentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> comments : params) {
            String commentsId = comments.get("commentsid");
            postService.deleteBoardCommentsWriteReportPost(commentsId);
            postService.deleteBoardCommentsReportPost(commentsId);
        }
        return "admin-reportcomments";
    }
    // 관리자페이지 신고 개인 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportindividualcommentsdelete")
    public String individualCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> individualcomments : params) {
            String commentsId = individualcomments.get("commentsid");
            postService.deleteIndividualCommentsWriteReportPost(commentsId);
            postService.deleteIndividualCommentsReportPost(commentsId);
        }
        return "admin-reportindividualcomments";
    }
    // 관리자페이지 신고 단체 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportgroupcommentsdelete")
    public String groupCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> groupcomments : params) {
            String commentsId = groupcomments.get("commentsid");
            postService.deleteGroupCommentsWriteReportPost(commentsId);
            postService.deleteGroupCommentsReportPost(commentsId);
        }
        return "admin-reportgroupcomments";
    }
    // 관리자페이지 신고 중고 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportusedcommentsdelete")
    public String usedCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> usedcomments : params) {
            String commentsId = usedcomments.get("commentsid");
            postService.deleteUsedCommentsWriteReportPost(commentsId);
            postService.deleteUsedCommentsReportPost(commentsId);
        }
        return "admin-reportusedcomments";
    }


//  커뮤니티글 신고 관리자 권한 조회
@PostMapping("/ribbon/admin/post/boardlogin")
public void adminBoardLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
    String userid = adminLoginRequestDto.getUserid();
    String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportboard", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/boardlogin");
        }
    }
    //  개인글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/individuallogin")
    public void adminIndividualLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportindividual", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/individuallogin");
        }
    }
    //  단체글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/grouplogin")
    public void adminGroupLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportgroup", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/grouplogin");
        }
    }
    //  중고글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/usedlogin")
    public void adminUsedLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportused", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/usedlogin");
        }
    }
    //  유저 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/userlogin")
    public void adminUserLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();

            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportuser", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/login");
            }

    }
    //  커뮤니티 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/commentslogin")
    public void adminCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportcomments", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/login");
            }
    }
    //  개인 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/individualcommentslogin")
    public void adminIndividualCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportindividualcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/login");
        }
    }
    //  단체 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/groupcommentslogin")
    public void adminGroupCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportgroupcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/login");
        }
    }
    //  중고 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/usedcommentslogin")
    public void adminUsedCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange("http://192.168.0.5:8000/ribbon/admin/reportusedcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/login");
        }
    }
}
