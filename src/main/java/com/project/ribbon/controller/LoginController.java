package com.project.ribbon.controller;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.PaymentData;
import com.project.ribbon.dto.PostBuyerInfoDTO;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;
import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.FirebaseAnnouncementMessageService;
import com.project.ribbon.service.MemberService;
import com.project.ribbon.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final PostService postService;
    private final FirebaseAnnouncementMessageService firebaseAnnouncementMessageService;
    // 서버 업로드용 ip
    @Value("${myapp.cafe24AdminIp}")
    private String cafe24AdminIp;
    // 서버업로드용 gif 파일 경로
    @Value("${myapp.cafe24AdminGif}")
    private String cafe24AdminGif;
    // 서버업로드용 이미지 파일 경로
    @Value("${myapp.cafe24AdminImg}")
    private String cafe24AdminImg;
    // 개발환경용 ip
    @Value("${myapp.developAdminIp}")
    private String developAdminIp;
    // 개발환경용 맺음 gif 파일 경로
    @Value("${myapp.developAdminGif}")
    private String developAdminGif;
    // 개발환경용 맺음 이미지 파일 경로
    @Value("${myapp.developAdminImg}")
    private String developAdminImg;
//    String ip = developAdminIp;

    // 맺음 홈페이지
    @GetMapping("/ribbon")
    public String showRibbonForm() {
        return "index";
    }
    @GetMapping("/ribbon/ribbon.gif")
    public ResponseEntity<byte[]> getRibbonGif() throws IOException {
        Path gifPath = Paths.get(developAdminGif);
        byte[] gifBytes = Files.readAllBytes(gifPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<>(gifBytes, headers, HttpStatus.OK);
    }
    @GetMapping("/ribbon/ribbonding.png")
    public ResponseEntity<byte[]> getRibbonImage() throws IOException {
        Path imagePath = Paths.get(developAdminImg);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // secrettoken발급
    @Value("${myapp.secretKey}")
    private String secretKey;
    @GetMapping("/ribbon/admin/secret")
    @ResponseBody
    public Map<String, Object> showAnnouncementLoginSecretForm() {
        Map<String, Object> response = new HashMap<>();
        response.put("secretKey", secretKey);
        return response;
    }
    // 결제정보 로그인 폼
    @GetMapping("/ribbon/admin/paymentlogin")
    public String showPaymentLoginForm() {
        return "admin-paymentlogin";
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

    // 멘토 글 신고 로그인 폼
    @GetMapping("/ribbon/admin/mentorlogin")
    public String showMentorLoginForm() {

        return "admin-mentorlogin";
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

    // 유저 결제 정보 조회
    @GetMapping("/ribbon/admin/paymentinfo")
    public String paymentInfo(Model model) {
        List<PostPaymentInfoResponse> paymentList = postService.findPaymentInfoAllPost();
        model.addAttribute("paymentList", paymentList);
        return "admin-paymentinfo";
    }

    // 유저 결제 정보 수정
    @PostMapping("/ribbon/admin/form/hidepaymentinfo")
    public String hidePaymentInfo(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> payment:params) {
            String paymentId = payment.get("paymentid");
            postService.updatePaymentInfoPost(paymentId);
        }
        return "admin-paymentinfo";
    }

    // 결제 문의 조회
    @GetMapping("/ribbon/admin/inquerypaymentinfo")
    public String inqueryPaymentiIfo(Model model) {
        List<PostPaymentInfoResponse> inquerypaymentList = postService.findInqueryPaymentInfoAllPost();
        model.addAttribute("inquerypaymentList", inquerypaymentList);
        return "admin-inquerypaymentinfo";
    }
    // 결제 문의에 대한 결제 취소 요청
    @Value("${myapp.impKey}")
    private String impKey;
    @Value("${myapp.impSecret}")
    private String impSecret;
    @PostMapping("/ribbon/admin/form/inquerypaymentinfocancel")
    public String inqueryPaymentInfoDelete(@RequestBody List<Map<String,String>> params) {
        try {
            for (Map<String, String> inquerypayment : params) {
                String merchant_uid = inquerypayment.get("merchantUid");

                    // 엑세스 토큰 발급
                    WebClient webClient = WebClient.builder().build();
                    String url = "https://api.iamport.kr/users/getToken";
                    Map<String, Object> request = new HashMap<>();
                    request.put("imp_key", impKey);
                    request.put("imp_secret", impSecret);
                    Mono<String> response = webClient.post()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(String.class);
                    String responseBody = response.block();
                    JsonElement responseJson = JsonParser.parseString(Objects.requireNonNull(responseBody));
                    JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
                    if (responseObj != null && !responseObj.isJsonNull()) {
                        String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
                        // 해쉬맵에 데이터 담기
                        Map<String, Object> data = new HashMap<>();

                        if (merchant_uid != null) {
                            data.put("merchant_uid", merchant_uid);
                        }
                        // API 호출 URL 구성
                        String apiUrl = "https://api.iamport.kr/payments/cancel";

                        // 각각의 가격에 대한 API 호출 실행
                        Mono<ResponseEntity<String>> apiResponse = null;

                        if (merchant_uid != null) {
                            apiResponse = webClient.post()
                                    .uri(apiUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                                    .bodyValue(data)
                                    .retrieve()
                                    .toEntity(String.class);
                        }
                        HttpStatusCode apiStatus = null;
                        if (apiResponse != null) {
                            apiStatus = Objects.requireNonNull(apiResponse.block()).getStatusCode();
                        }
                        if (Objects.requireNonNull(apiStatus).is2xxSuccessful()) {

                            String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid;
                            PaymentData paymentData = webClient.get()
                                    .uri(paymentUrl)
                                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                                    .retrieve()
                                    .bodyToMono(PaymentData.class)
                                    .block();

                            String status = Objects.requireNonNull(paymentData).getResponse().getStatus();
                            // 결과를 처리하는 로직
                            switch (status) {
                                case "paid" -> {
                                    return ResponseEntity.ok("결제 완료 상태입니다.").toString();
                                }
                                case "ready" -> {
                                    return ResponseEntity.ok("결제 준비 상태입니다.").toString();
                                }
                                case "failed" -> {
                                    return ResponseEntity.ok("결제 실패 상태입니다.").toString();
                                }
                                case "cancelled" -> {
                                    PostBuyerInfoDTO deleteparams = new PostBuyerInfoDTO();
                                    deleteparams.setMerchantUid(merchant_uid);
                                    postService.deleteBuyerInfoPost(deleteparams);
                                    postService.deletePaidInfoPost(deleteparams);
                                    return "admin-inquerypaymentinfo";
                                }
                                default -> {
                                    return ResponseEntity.badRequest().build().toString();
                                }
                            }
                        }
                        return ResponseEntity.ok().build().toString();

                    } else {
                        throw new RuntimeException("Access token not found in response");
                    }
                }
                }catch(Exception e){
                    // 취소 실패
                    return ResponseEntity.badRequest().build().toString();
                }
        return "admin-inquerypaymentinfo";
    }

    // 문의하기 조회
    @GetMapping("/ribbon/admin/inquiryinfo")
    public String inquiryInfo(Model model) {
        List<PostReportBoardResponse> inquiryList = postService.findInquiryAllPost();
        model.addAttribute("inquiryList", inquiryList);
        return "admin-inquiryinfo";
    }
    // 공지사항 정보 저장
    @PostMapping("/ribbon/admin/announcement")
    public ResponseEntity<?> announcementInsert(@RequestBody PostAnnouncementRequest params) throws IOException{
        firebaseAnnouncementMessageService.sendMessageTo(
                params.getTitle(),
                params.getContent());
        ResponseEntity.ok().build().getStatusCode();
        return new ResponseEntity<>(postService.saveAnnouncementPost(params),HttpStatus.OK);
    }
    // 공지사항 입력 폼
    @GetMapping("/ribbon/admin/announcement")
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
    @DeleteMapping("/ribbon/admin/form/adminannouncement")
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
    @PostMapping("/ribbon/admin/reportuser")
    public ResponseEntity<?> userReportInsert(@RequestBody PostReportUserResponse params) {
        return new ResponseEntity<>(postService.saveReportUserPost(params),HttpStatus.OK);
    }
    // 신고 멘토글 정보 조회
    @GetMapping("/ribbon/admin/reportmentor")
    public String mentorReport(Model model) {
        List<PostWritementorDTO> mentorList = postService.findReportMentorAllPost();
        model.addAttribute("mentorList", mentorList);
        return "admin-reportpostdeletementor";
    }
    // 신고 멘토글 정보 저장
    @PostMapping("/ribbon/admin/reportmentor")
    public ResponseEntity<?> mentorReportInsert(@RequestBody PostWritementorDTO params) {
        return new ResponseEntity<>(postService.saveReportMentorPost(params), HttpStatus.OK);
    }
    // 신고 커뮤니티글 정보 조회
    @GetMapping("/ribbon/admin/reportboard")
    public String boardReport(Model model) {
        List<PostReportBoardResponse> boardList = postService.findReportBoardAllPost();
        model.addAttribute("boardList", boardList);
        return "admin-reportpostdeleteboard";
    }
    // 신고 커뮤니티글 정보 저장
    @PostMapping("/ribbon/admin/reportboard")
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
    @PostMapping("/ribbon/admin/reportindividual")
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
    @PostMapping("/ribbon/admin/reportgroup")
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
    @PostMapping("/ribbon/admin/reportused")
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
    @PostMapping("/ribbon/admin/reportcomments")
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
    @PostMapping("/ribbon/admin/reportindividualcomments")
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
    @PostMapping("/ribbon/admin/reportgroupcomments")
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
    @PostMapping("/ribbon/admin/reportusedcomments")
    public ResponseEntity<?> usedcommentsReportInsert(@RequestBody PostReportCommentsResponse params) {
        return new ResponseEntity<>(postService.saveReportUsedCommentsPost(params),HttpStatus.OK);
    }
    // 신고 유저 정보 수정
    @PutMapping("/ribbon/admin/form/reportuser")
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
    @PutMapping("/ribbon/admin/form/reportuseractivate")
    public String userActivateUpdate(@RequestBody List<Map<String,String>> params) {
        for (Map<String,String> user:params) {
            String userId = user.get("userid");
            postService.updateActivateUserPost(userId);
            postService.deleteActivateUserPost(userId);
        }
        return "admin-reportpostdeleteuser";
    }
    // 관리자페이지 신고 멘토 글 정보 삭제
    @DeleteMapping("/ribbon/admin/form/reportmentor")
    public String mentorReportDelete(@RequestBody List<Map<String,String>> params) {
        System.out.println(params);
        for (Map<String, String> mentor : params) {
            String id = mentor.get("id");
            postService.deleteMentorWriteReportPost(id);
            postService.deleteMentorReportPost(id);
            postService.deleteMentorReviewWriteReportPost(id);
        }
        return "admin-reportpostdeletementor";
    }
    // 관리자페이지 신고 커뮤니티글 정보 삭제
    @DeleteMapping("/ribbon/admin/form/reportboard")
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
    @DeleteMapping("/ribbon/admin/form/reportindividual")
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
    @DeleteMapping("/ribbon/admin/form/reportgroup")
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
    @DeleteMapping("/ribbon/admin/form/reportused")
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
    @DeleteMapping("/ribbon/admin/form/reportcomments")
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
    @DeleteMapping("/ribbon/admin/form/reportindividualcomments")
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
    @DeleteMapping("/ribbon/admin/form/reportgroupcomments")
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
    @DeleteMapping("/ribbon/admin/form/reportusedcomments")
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
    //  결제정보 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/paymentlogin")
    public void adminPaymentLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/paymentinfo", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/paymentlogin");
        }
    }
    //  문의하기 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/inquirylogin")
    public void adminInquiryLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/inquiryinfo", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/inquirylogin");
        }
    }

    //  멘토 글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/mentorlogin")
    public void adminMentorLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportmentor", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/mentorlogin");
        }
    }
//  커뮤니티글 신고 관리자 권한 조회
@PostMapping("/ribbon/admin/form/boardlogin")
public void adminBoardLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
    String userid = adminLoginRequestDto.getUserid();
    String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportboard", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/boardlogin");
        }
    }
    //  개인글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/individuallogin")
    public void adminIndividualLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportindividual", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/individuallogin");
        }
    }
    //  단체글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/grouplogin")
    public void adminGroupLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportgroup", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/grouplogin");
        }
    }
    //  중고글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/usedlogin")
    public void adminUsedLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportused", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/usedlogin");
        }
    }
    //  유저 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/userlogin")
    public void adminUserLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();

            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportuser", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/userlogin");
            }

    }
    //  커뮤니티 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/commentslogin")
    public void adminCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
            TokenInfo tokenInfo = memberService.login(userid, password);
            if (tokenInfo != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportcomments", HttpMethod.GET, entity, String.class);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendRedirect("/ribbon/admin/commentslogin");
            }
    }
    //  개인 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/individualcommentslogin")
    public void adminIndividualCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportindividualcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/individualcommentslogin");
        }
    }
    //  단체 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/groupcommentslogin")
    public void adminGroupCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportgroupcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/groupcommentslogin");
        }
    }
    //  중고 댓글 신고 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/usedcommentslogin")
    public void adminUsedCommentsLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/reportusedcomments", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/usedcommentslogin");
        }
    }

    //  공지사항 관리자 권한 조회
    @PostMapping("/ribbon/admin/form/announcement")
    public void adminAnnouncementLogin(@RequestBody AdminLoginRequestDto adminLoginRequestDto, HttpServletResponse response) throws IOException {
        String userid = adminLoginRequestDto.getUserid();
        String password = adminLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        if (tokenInfo != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(developAdminIp+"/announcement", HttpMethod.GET, entity, String.class);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect("/ribbon/admin/announcement");
        }
    }

}
