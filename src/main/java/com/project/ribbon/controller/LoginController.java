package com.project.ribbon.controller;


import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;
import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.FirebaseAnnouncementMessageService;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;
    private final FirebaseAnnouncementMessageService firebaseAnnouncementMessageService;
    // 서버 업로드용 ip : https://ribbonding.shop:48610/ribbon/admin
    // 개발환경용 ip : https://192.168.219.161:8000/ribbon/admin
    String ip = "https://192.168.219.161:8000/ribbon/admin";
    // 맺음 홈페이지
    @GetMapping("/ribbon")
    public String showRibbonForm() {
        return "index";
    }
    @GetMapping("/ribbon/ribbon.png")
    public ResponseEntity<byte[]> getRibbonImage() throws IOException {
        Path imagePath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/src/main/resources/static/ribbon.png" );
        byte[] imageBytes = Files.readAllBytes(imagePath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    // 문의하기 로그인 폼
    @GetMapping("/ribbon/admin/inquirylogin")
    public String showInquiryLoginForm() {
        return "admin-inquirylogin";
    }
    // 공지사항 로그인 폼
    @GetMapping("/ribbon/admin/announcementlogin")
    public String showAnnouncementLoginForm() {
        return "admin-announcementlogin";
    }
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
    // 신고 및 공지 및 문의 조회 폼
    @GetMapping("/ribbon/admin/report")
    public String showReportForm() {
        return "admin-report";
    }

    // 문의하기 조회
    @GetMapping("/ribbon/admin/inquiryinfo")
    public String inquiryinfo(Model model) {
        List<PostReportBoardResponse> inquiryList = postService.findInquiryAllPost();
        model.addAttribute("inquiryList", inquiryList);
        return "admin-inquiryinfo";
    }
    // 공지사항 정보 저장
    @PostMapping("/ribbon/admin/postinsertannouncement")
    public ResponseEntity<?> announcementInsert(@RequestBody PostAnnouncementRequest params) throws IOException{
        firebaseAnnouncementMessageService.sendMessageTo(
                params.getTitle(),
                params.getContent());
        ResponseEntity.ok().build().getStatusCodeValue();
        return new ResponseEntity<>(postService.saveAnnouncementPost(params),HttpStatus.OK);
    }
    // 공지사항 입력 폼
    @GetMapping("/ribbon/admin/insertannouncement")
    public String announcementInsert() {
        return "admin-announcement";
    }
    // 공지사항 조회
    @GetMapping("/ribbon/admin/announcementinfo")
    public ResponseEntity<?> announcementInfo(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostAnnouncementRequest> posts = postService.findAnnouncementAllPost();
        model.addAttribute("posts", posts);
        obj.put("announcementinfo", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 관리자 페이지 공지사항 조회
    @GetMapping("/ribbon/admin/adminannouncementinfo")
    public String adminannouncementInfo(Model model) {
        List<PostAnnouncementRequest> announcementList = postService.findAnnouncementAllPost();
        model.addAttribute("announcementList", announcementList);
        return "admin-adminannouncement";
    }
    // 관리자페이지 공지사항 삭제
    @RequestMapping("/ribbon/admin/post/adminannouncementdelete")
    public String adminannouncementInfoDelete(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> announcement:params) {
            String id = announcement.get("id");
            postService.deleteAdminAnnouncementPost(id);
        }
        return "admin-adminannouncement";
    }

    // 신고 유저 정보 조회
    @GetMapping("/ribbon/admin/reportuser")
    public String userReport(Model model) {
        List<PostReportUserResponse> userList = postService.findReportUserAllPost();
        model.addAttribute("userList", userList);
        return "admin-reportpostdeleteuser";
    }
    // 신고 유저 정보 저장
    @PostMapping("/ribbon/admin/reportinsertuser")
    public ResponseEntity<?> userReportInsert(@RequestBody PostReportUserResponse params) {
        return new ResponseEntity<>(postService.saveReportUserPost(params),HttpStatus.OK);
    }
    // 신고 커뮤니티글 정보 조회
    @GetMapping("/ribbon/admin/reportboard")
    public String boardReport(Model model) {
        List<PostReportBoardResponse> boardList = postService.findReportBoardAllPost();
        model.addAttribute("boardList", boardList);
        return "admin-reportpostdeleteboard";
    }
    // 신고 커뮤니티글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertboard")
    public ResponseEntity<?> boardReportInsert(@RequestBody PostReportBoardResponse params) {
        return new ResponseEntity<>(postService.saveReportBoardPost(params), HttpStatus.OK);
    }
    // 신고 개인글 정보 조회
    @GetMapping("/ribbon/admin/reportindividual")
    public String individualReport(Model model) {
        List<PostReportIndividualResponse> individualList = postService.findReportIndividualAllPost();
        model.addAttribute("individualList", individualList);
        return "admin-reportpostdeleteindividual";
    }
    // 신고 개인글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertindividual")
    public ResponseEntity<?> individualReportInsert(@RequestBody PostReportIndividualResponse params) {
        return new ResponseEntity<>(postService.saveReportIndividualPost(params),HttpStatus.OK);
    }
    // 신고 단체글 정보 조회
    @GetMapping("/ribbon/admin/reportgroup")
    public String groupReport(Model model) {
        List<PostReportGroupResponse> groupList = postService.findReportGroupAllPost();
        model.addAttribute("groupList", groupList);
        return "admin-reportpostdeletegroup";
    }
    // 신고 단체글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertgroup")
    public ResponseEntity<?> groupReportInsert(@RequestBody PostReportGroupResponse params) {
        return new ResponseEntity<>(postService.saveReportGroupPost(params),HttpStatus.OK);
    }
    // 신고 중고글 정보 조회
    @GetMapping("/ribbon/admin/reportused")
    public String usedReport(Model model) {
        List<PostReportUsedResponse> usedList = postService.findReportUsedAllPost();
        model.addAttribute("usedList", usedList);
        return "admin-reportpostdeleteused";
    }
    // 신고 중고글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertused")
    public ResponseEntity<?> usedReportInsert(@RequestBody PostReportUsedResponse params) {

        return new ResponseEntity<>(postService.saveReportUsedPost(params),HttpStatus.OK);
    }
    // 신고 커뮤니티 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportcomments")
    public String commentsReport(Model model) {
        List<PostReportCommentsResponse> commentsList = postService.findReportCommentsAllPost();
        model.addAttribute("commentsList", commentsList);
        return "admin-reportcomments";
    }
    // 신고 커뮤니티 댓글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertcomments")
    public ResponseEntity<?> commentsReportInsert(@RequestBody PostReportCommentsResponse params) {
        return new ResponseEntity<>(postService.saveReportCommentsPost(params),HttpStatus.OK);
    }
    // 신고 개인 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportindividualcomments")
    public String individualcommentsReport(Model model) {
        List<PostReportCommentsResponse> individualcommentsList = postService.findReportIndividualCommentsAllPost();
        model.addAttribute("individualcommentsList", individualcommentsList);
        return "admin-reportindividualcomments";
    }
    // 신고 개인 댓글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertindividualcomments")
    public ResponseEntity<?> individualcommentsReportInsert(@RequestBody PostReportCommentsResponse params) {
        return new ResponseEntity<>(postService.saveReportIndividualCommentsPost(params),HttpStatus.OK);
    }
    // 신고 단체 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportgroupcomments")
    public String groupcommentsReport(Model model) {
        List<PostReportCommentsResponse> groupcommentsList = postService.findReportGroupCommentsAllPost();
        model.addAttribute("groupcommentsList", groupcommentsList);
        return "admin-reportgroupcomments";
    }
    // 신고 단체 댓글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertgroupcomments")
    public ResponseEntity<?> groupcommentsReportInsert(@RequestBody PostReportCommentsResponse params) {
        return new ResponseEntity<>(postService.saveReportGroupCommentsPost(params),HttpStatus.OK);
    }
    // 신고 중고 댓글 정보 조회
    @GetMapping("/ribbon/admin/reportusedcomments")
    public String usedcommentsReport(Model model) {
        List<PostReportCommentsResponse> usedcommentsList = postService.findReportUsedCommentsAllPost();
        model.addAttribute("usedcommentsList", usedcommentsList);
        return "admin-reportusedcomments";
    }
    // 신고 중고 댓글 정보 저장
    @PostMapping("/ribbon/admin/reportinsertusedcomments")
    public ResponseEntity<?> usedcommentsReportInsert(@RequestBody PostReportCommentsResponse params) {
        return new ResponseEntity<>(postService.saveReportUsedCommentsPost(params),HttpStatus.OK);
    }
    // 신고 유저 정보 수정
    @RequestMapping("/ribbon/admin/post/reportuserdelete")
    public String userReportDelete(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> user:params) {
            String userId = user.get("userid");
            postService.updateReportUserRolesPost(userId);
            postService.updateReportUserPost(userId);
            postService.updateUserReportPost(userId);
        }
        return "admin-reportpostdeleteuser";
    }
    // 활성화 유저 정보 수정 및 삭제
    @RequestMapping("/ribbon/admin/post/reportuseractivate")
    public String userActivateUpdate(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> user:params) {
            String userId = user.get("userid");
            postService.updateActivateUserPost(userId);
            postService.deleteActivateUserPost(userId);
        }
        return "admin-reportpostdeleteuser";
    }
    // 관리자페이지 신고 커뮤니티글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportboarddelete")
    public String boardReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> board : params) {
            String boardId = board.get("boardid");
            postService.deleteBoardReportPost(boardId);
            postService.deleteBoardWriteReportPost(boardId);
        }
        return "admin-reportpostdeleteboard";
    }
    // 관리자페이지 신고 개인글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportindividualdelete")
    public String individualReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> individual : params) {
            String individualId = individual.get("individualid");
            postService.deleteIndividualReportPost(individualId);
            postService.deleteIndividualWriteReportPost(individualId);
        }
        return "admin-reportpostdeleteindividual";
    }
    // 관리자페이지 신고 단체글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportgroupdelete")
    public String groupReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> group : params) {
            String groupId = group.get("groupid");
            postService.deleteGroupReportPost(groupId);
            postService.deleteGroupWriteReportPost(groupId);
        }
        return "admin-reportpostdeletegroup";
    }
    // 관리자페이지 신고 중고글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportuseddelete")
    public String usedReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> used : params) {
            String usedId = used.get("usedid");
            postService.deleteUsedReportPost(usedId);
            postService.deleteUsedWriteReportPost(usedId);
        }
        return "admin-reportpostdeleteused";
    }

    // 관리자페이지 신고 커뮤니티 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportcommentsdelete")
    public String commentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> comments : params) {
            String commentsId = comments.get("commentsid");
            String boardId = comments.get("inherentid");
            postService.deleteBoardCommentsReportPost(commentsId);
            postService.updateDeleteReportCommentsCountPost(boardId);
            postService.deleteBoardCommentsWriteReportPost(commentsId);
        }
        return "admin-reportcomments";
    }
    // 관리자페이지 신고 개인 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportindividualcommentsdelete")
    public String individualCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> individualcomments : params) {
            String commentsId = individualcomments.get("commentsid");
            String individualId = individualcomments.get("inherentid");
            postService.deleteIndividualCommentsReportPost(commentsId);
            postService.updateDeleteReportIndividualCommentsCountPost(individualId);
            postService.deleteIndividualCommentsWriteReportPost(commentsId);
        }
        return "admin-reportindividualcomments";
    }
    // 관리자페이지 신고 단체 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportgroupcommentsdelete")
    public String groupCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> groupcomments : params) {
            String commentsId = groupcomments.get("commentsid");
            String groupId = groupcomments.get("inherentid");
            postService.deleteGroupCommentsReportPost(commentsId);
            postService.updateDeleteReportGroupCommentsCountPost(groupId);
            postService.deleteGroupCommentsWriteReportPost(commentsId);
        }
        return "admin-reportgroupcomments";
    }
    // 관리자페이지 신고 중고 댓글 정보 삭제
    @RequestMapping("/ribbon/admin/post/reportusedcommentsdelete")
    public String usedCommentsReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> usedcomments : params) {
            String commentsId = usedcomments.get("commentsid");
            String usedId = usedcomments.get("inherentid");
            postService.deleteUsedCommentsReportPost(commentsId);
            postService.updateDeleteReportUsedCommentsCountPost(usedId);
            postService.deleteUsedCommentsWriteReportPost(commentsId);
        }
        return "admin-reportusedcomments";
    }
    //  문의하기 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/inquirylogin")
    public void adminInquiryLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(ip+"/inquiryinfo", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/inquirylogin");
        }
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportboard", HttpMethod.GET, entity, String.class);
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportindividual", HttpMethod.GET, entity, String.class);
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportgroup", HttpMethod.GET, entity, String.class);
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportused", HttpMethod.GET, entity, String.class);
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
                ResponseEntity<String> result = restTemplate.exchange(ip+"/reportuser", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/userlogin");
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
                ResponseEntity<String> result = restTemplate.exchange(ip+"/reportcomments", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/commentslogin");
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportindividualcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/individualcommentslogin");
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportgroupcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/groupcommentslogin");
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
            ResponseEntity<String> result = restTemplate.exchange(ip+"/reportusedcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/usedcommentslogin");
        }
    }

    //  공지사항 관리자 권한 조회
    @PostMapping("/ribbon/admin/post/announcement")
    public void adminAnnouncementLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(ip+"/insertannouncement", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/announcement");
        }
    }
}
