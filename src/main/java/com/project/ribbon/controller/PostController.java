package com.project.ribbon.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.ribbon.customvaild.DigitLength;
import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.PostBuyerInfoDTO;
import com.project.ribbon.dto.ReissueToken;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;
import com.project.ribbon.filter.JwtAuthenticationFilter;
import com.project.ribbon.provide.JwtTokenProvider;
import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@ResponseBody
@Slf4j
@RestController
@Validated
@ControllerAdvice
public class PostController {


    private CsrfTokenRepository csrfTokenRepository;
    private final PostService postService;
    private final FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;
    private final FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;
    private final FirebaseCloudChatMessageService firebaseCloudChatMessageService;

    private final MemberService memberService;

    @Autowired
    @Qualifier("jwtTokenProvider")
    private JwtTokenProvider jwtTokenProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    // 서버업로드용 서버 ip : https://ribbonding.shop:48610
    // 서버업로드용 이미지 파일 경로 : /oxen6297/tomcat/webapps/ROOT/image/
    // 개발환경용 서버 ip : http://112.148.33.214:8000
    // 개발환경용 이미지 파일 경로 : /Users/gim-yong-won/Desktop/ribbon/image/
    String userip = "http://112.148.33.214:8000/api/userimage/";
    String boardip = "http://112.148.33.214:8000/api/boardimage/";
    String groupip = "http://112.148.33.214:8000/api/groupimage/";
    String usedip = "http://112.148.33.214:8000/api/usedimage/";
    String mentorip = "http://112.148.33.214:8000/api/writementortitleimage/";

    @Value("${file.upload.path}")
    private String uploadPath;

    // 커뮤니티 게시글 작성
    @PostMapping("/post/boardwrite")
    public ResponseEntity<?> savePost(
            @RequestParam("id") @NotNull(message = "카테고리 필수입력값입니다.") Integer id,
            @RequestParam("userid") @NotNull(message = "유저아이디 필수입력값입니다.") Long userId,
            @RequestParam("title") @NotBlank(message = "제목 필수입력값입니다.") @Size(min = 2, max = 30) String title,
            @RequestParam("description") @NotBlank(message = "내용 필수입력값입니다.") String description,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("writedate") @Size(min = 2, max = 30) String writeDate,
            @RequestParam("nickname") @NotBlank(message = "닉네임 필수입력값입니다.") String nickname
    ) throws IOException {
        PostRequest params = new PostRequest();
        params.setId(id);
        params.setUserid(userId);
        params.setTitle(title);
        params.setDescription(description);
        params.setWritedate(writeDate);
        params.setNickname(nickname);

        if (file != null) {
            String filename = file.getOriginalFilename();
            String imagePath = Paths.get(uploadPath, filename).toString();
            byte[] bytes = file.getBytes();
            Path filePath = Paths.get(imagePath);
            Files.write(filePath, bytes);
            params.setImg(boardip + filename);
        } else {
            params.setImg(null);
        }
        return ResponseEntity.ok(postService.savePost(params));
    }


    // 커뮤니티 프로필 사진 조회
    @GetMapping("/boardimage/{imageName:.+}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable("imageName") String img) throws IOException {
        Path imageBoardPath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/image/" + img);
        byte[] imageBytes = Files.readAllBytes(imageBoardPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 커뮤니티 게시글 조회
    @GetMapping("/board")
    public ResponseEntity<?> boardWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost();
        model.addAttribute("posts", posts);
        obj.put("boardwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 커뮤니티 특정 게시글 조회
    @PostMapping("/board")
    public ResponseEntity<?> boardWriteOne(@RequestBody PostResponse params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateBoardInquiryPost(params.getBoardid());
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findOnePost(params.getBoardid());
        model.addAttribute("posts", posts);
        obj.put("boardwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 기존 게시글 수정
    @PostMapping("/post/update")
    public ResponseEntity<?> updatePost(@RequestBody PostRequest params) throws ApiException {
        try {
            return postService.updatePost(params);
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    // 기존 게시글 삭제
    @DeleteMapping("/post/delete")
    public ResponseEntity<?> deletePost(@RequestBody PostRequest params) {
        try {
            postService.deleteBoardWriteCommentsPost(params);
            postService.deleteBoardWriteLikedPost(params);
            return postService.deletePost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 단체 작성글 조회
    @GetMapping("/group")
    public ResponseEntity<?> groupWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupAllPost();
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 단체 특정 작성글 조회
    @PostMapping("/group")
    public ResponseEntity<?> groupWriteOne(@RequestBody PostGroupResponse params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateGroupInquiryPost(params.getGroupid());
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupOnePost(params.getGroupid());
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 단체 글작성
    @PostMapping("/post/writegroup")
    public ResponseEntity<?> saveGroupPost(
            @RequestParam("id") @NotNull(message = "카테고리 필수입력값입니다.") Integer id,
            @RequestParam("region") @NotBlank(message = "지역 필수입력값입니다.") String region,
            @RequestParam("title") @Size(min = 2, max = 30) @NotBlank(message = "제목 필수입력값입니다.") String title,
            @RequestParam("line") @Size(min = 2, max = 40) String line,
            @RequestParam("description") @NotBlank(message = "내용 필수입력값입니다.") String description,
            @RequestParam("peoplenum") @NotNull(message = "인원수 필수입력값입니다.") String peoplenum,
            @RequestParam("gender") @NotBlank(message = "성별 필수입력값입니다.") String gender,
            @RequestParam("minage") @NotNull(message = "최소나이 필수입력값입니다.") String minage,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("userid") @NotNull(message = "유저아이디 필수입력값입니다.") Long userid,
            @RequestParam("maxage") @NotNull(message = "최대나이 필수입력값입니다.") String maxage,
            @RequestParam("writedate") @NotBlank(message = "작성날짜 필수입력값입니다.") String writedate,
            @RequestParam("peoplenownum") @NotBlank(message = "현재인원 필수입력값입니다.") String peoplenownum,
            @RequestParam("nickname") @NotBlank(message = "닉네임 필수입력값입니다.") String nickname,
            @RequestParam("once") @NotBlank(message = "일,다회성 필수입력값입니다.") String once
    ) throws IOException {
        PostGroupRequest params = new PostGroupRequest();
        params.setId(id);
        params.setRegion(region);
        params.setTitle(title);
        params.setLine(line);
        params.setDescription(description);
        params.setPeoplenum(peoplenum);
        params.setGender(gender);
        params.setMinage(minage);
        params.setUserid(userid);
        params.setMaxage(maxage);
        params.setWritedate(writedate);
        params.setPeoplenownum(peoplenownum);
        params.setNickname(nickname);
        params.setOnce(once);

        if (file != null) {
            String titleimage = file.getOriginalFilename();
            String filegrouppath = Paths.get(uploadPath, titleimage).toString();
            byte[] bytes = file.getBytes();
            Path groupimagepath = Paths.get(filegrouppath);
            Files.write(groupimagepath, bytes);
            params.setTitleimage(groupip + titleimage);
        } else {
            params.setTitleimage(null);
        }

        return new ResponseEntity<>(postService.saveGroupPost(params), HttpStatus.OK);
    }

    // 단체 프로필 사진 조회
    @GetMapping("/groupimage/{imageName:.+}")
    public ResponseEntity<byte[]> getGroupImage(@PathVariable("imageName") String titleimage) throws IOException {
        Path imageGroupPath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/image/" + titleimage);
        byte[] imageBytes = Files.readAllBytes(imageGroupPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 단체 게시글 수정
    @PostMapping("/post/updategroup")
    public ResponseEntity<?> updateGroupPost(@RequestBody PostGroupRequest params) {
        try {
            return postService.updateGroupPost(params);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
        }
    }

    // 단체 게시글 삭제
    @DeleteMapping("/post/deletegroup")
    public ResponseEntity<?> deleteGroupPost(@RequestBody PostGroupRequest params) {
        try {
            postService.deleteGroupWriteCommentsPost(params);
            return postService.deleteGroupPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred while deleting group post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 작성글 조회
    @GetMapping("/individual")
    public ResponseEntity<?> indiWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiAllPost();
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 개인 특정 작성글 조회
    @PostMapping("/individual")
    public ResponseEntity<?> indiWriteOne(@RequestBody PostIndiResponse params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateIndiInquiryPost(params.getIndividualid());
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiOnePost(params.getIndividualid());
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 개인 글작성
    @PostMapping("/post/writeindividual")
    public ResponseEntity<?> saveIndiPost(@RequestBody @Valid PostIndiRequest params) throws ApiException {
        try {
            return new ResponseEntity<>(postService.saveIndiPost(params), HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 게시글 수정
    @PostMapping("/post/updateindividual")
    public ResponseEntity<?> updateIndiPost(@RequestBody PostIndiRequest params) {
        try {
            return postService.updateIndiPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 게시글 삭제
    @DeleteMapping("/post/deleteindividual")
    public ResponseEntity<?> deleteIndiPost(@RequestBody PostIndiRequest params) {
        try {
            postService.deleteIndiWriteCommentsPost(params);
            postService.deleteIndiWriteLikedPost(params);
            return postService.deleteIndiPost(params);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the post.");
        }
    }


    // 중고 작성글 조회
    @GetMapping("/used")
    public ResponseEntity<?> usedWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedAllPost();
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 중고 특정 작성글 조회
    @PostMapping("/used")
    public ResponseEntity<?> usedWriteOne(@RequestBody PostUsedResponse params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateUsedInquiryPost(params.getUsedid());
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedOnePost(params.getUsedid());
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 중고 글작성
    @PostMapping("/post/writeused")
    public ResponseEntity<?> saveUsedPost(
            @RequestParam("id") @NotNull(message = "카테고리는 필수 입력값입니다.") Integer id,
            @RequestParam("region") @NotBlank(message = "지역은 필수 입력값입니다.") String region,
            @RequestParam("title") @Size(min = 2, max = 30, message = "제목은 2~30자리여야 합니다.") @NotBlank(message = "제목은 필수 입력 값입니다.") String title,
            @RequestParam("description") @NotBlank(message = "내용은 필수 입력 값입니다.") @Size(min = 2, max = 200, message = "내용은 2~200자리여야 합니다.") String description,
            @RequestParam(value = "usedimage1", required = false) MultipartFile file1,
            @RequestParam("price") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 4, max = 8, message = "가격은 4~7자리로 입력해주세요.") Integer price,
            @RequestParam("userid") @NotNull(message = "유저아이디는 필수 입력값입니다.") Long userid,
            @RequestParam("writedate") @NotBlank(message = "작성날짜는 필수 입력값입니다.") String writedate,
            @RequestParam("nickname") @NotBlank(message = "닉네임은 필수 입력 값입니다.") String nickname,
            @RequestParam(value = "usedimage2", required = false) MultipartFile file2,
            @RequestParam(value = "usedimage3", required = false) MultipartFile file3,
            @RequestParam(value = "usedimage4", required = false) MultipartFile file4,
            @RequestParam(value = "usedimage5", required = false) MultipartFile file5) throws IOException {

        List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
        List<String> usedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null) {
                String usedimage = file.getOriginalFilename();
                String fileusedpath = Paths.get(uploadPath, usedimage).toString();
                byte[] bytes = file.getBytes();
                Path groupimagepath = Paths.get(fileusedpath);
                Files.write(groupimagepath, bytes);
                usedImages.add(usedip + usedimage);
            }
        }
        PostUsedRequest params = new PostUsedRequest();
        params.setId(id);
        params.setRegion(region);
        params.setTitle(title);
        params.setDescription(description);
        params.setPrice(price);
        params.setUserid(userid);
        params.setWritedate(writedate);
        params.setNickname(nickname);
        if (!usedImages.isEmpty()) {
            params.setUsedimage1(usedImages.get(0));
        }
        if (usedImages.size() > 1) {
            params.setUsedimage2(usedImages.get(1));
        }
        if (usedImages.size() > 2) {
            params.setUsedimage3(usedImages.get(2));
        }
        if (usedImages.size() > 3) {
            params.setUsedimage4(usedImages.get(3));
        }
        if (usedImages.size() > 4) {
            params.setUsedimage5(usedImages.get(4));
        }
        return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
    }


    // 중고 사진 조회
    @GetMapping("/usedimage/{imageName:.+}")
    public ResponseEntity<byte[]> getUsedImage(@PathVariable("imageName") String usedimage) throws IOException {
        Path imageUsedPath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/image/" + usedimage);
        byte[] imageBytes = Files.readAllBytes(imageUsedPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 중고 게시글 수정
    @PostMapping("/post/updateused")
    public ResponseEntity<?> updateUsedPost(@RequestBody PostUsedRequest params) {
        try {
            return postService.updateUsedPost(params);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    // 중고 게시글 삭제
    @DeleteMapping("/post/deleteused")
    public ResponseEntity<?> deleteUsedPost(@RequestBody PostUsedRequest params) {
        try {
            postService.deleteUsedWriteCommentsPost(params);
            postService.deleteUsedWriteLikedPost(params);
            return postService.deleteUsedPost(params);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Value("${myapp.impKey}")
    private String impKey;
    @Value("${myapp.impSecret}")
    private String impSecret;

    // 멘토 게시글 작성
    @PostMapping("/post/writementor")
    public ResponseEntity<?> saveWriteMentorPost(
            @RequestParam("title") @Size(min = 2, max = 30) String title,
            @RequestParam("category") @Size(min = 2, max = 10) String category,
            @RequestParam("shortcontent") @Size(min = 2, max = 60) String shortcontent,
            @RequestParam("description") @Size(min = 2, max = 1000) String description,
            @RequestParam("lowdescription") @Size(min = 2, max = 100) String lowdescription,
            @RequestParam("middledescription") @Size(min = 2, max = 100) String middledescription,
            @RequestParam("highdescription") @Size(min = 2, max = 100) String highdescription,
            @RequestParam("career") @Size(min = 2, max = 200) String career,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("writedate") String writedate,
            @RequestParam("lowprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer lowprice,
            @RequestParam("middleprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer middleprice,
            @RequestParam("highprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer highprice,
            @RequestParam("userid") Long userid,
            @RequestParam("nickname") @Size(min = 2, max = 10) String nickname,
            @RequestParam("region") @Size(min = 2, max = 20) String region,
            @RequestParam("contacttime") @Size(min = 2, max = 30) String contacttime
    ) {
        try {
            PostWritementorDTO params = new PostWritementorDTO();
            params.setTitle(title);
            params.setCategory(category);
            params.setShortcontent(shortcontent);
            params.setDescription(description);
            params.setLowdescription(lowdescription);
            params.setMiddledescription(middledescription);
            params.setHighdescription(highdescription);
            params.setCareer(career);
            params.setWritedate(writedate);
            params.setLowprice(lowprice);
            params.setMiddleprice(middleprice);
            params.setHighprice(highprice);
            params.setUserid(userid);
            params.setNickname(nickname);
            params.setRegion(region);
            params.setContacttime(contacttime);

            if (file != null) {
                String filename = file.getOriginalFilename();
                String imagePath = Paths.get(uploadPath, filename).toString();
                byte[] bytes = file.getBytes();
                Path filePath = Paths.get(imagePath);
                Files.write(filePath, bytes);
                params.setTitleimage(mentorip + filename);
            } else {
                params.setTitleimage(null);
            }
            postService.saveWritementorPost(params);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok("요청이 성공적으로 처리되었습니다.");
    }

    // 포트원 주문번호, 금액 사전 등록, 디비 저장
    @PostMapping("/post/pricebeforehandandsavebuyerinfo")
    public ResponseEntity<String> preparePayment(@RequestParam("userid") Long userid,
                                                 @RequestParam("username") @Size(min = 2, max = 10) String username,
                                                 @RequestParam("lowprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer lowprice,
                                                 @RequestParam("middleprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer middleprice,
                                                 @RequestParam("highprice") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer highprice,
                                                 @RequestParam("merchantUidLow") String merchantUidLow,
                                                 @RequestParam("merchantUidMiddle") String merchantUidMiddle,
                                                 @RequestParam("merchantUidHigh") String merchantUidHigh,
                                                 @RequestParam("inherentid") Long inherentid) throws IOException{
        try {

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
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
            JsonElement accessTokenJson = responseObj.get("access_token");
            if (accessTokenJson != null && !accessTokenJson.isJsonNull()) {
                String accessToken = "Bearer " + accessTokenJson.getAsString();
                Map<String, Object> dataLow = new HashMap<>();
                dataLow.put("amount", lowprice);
                if (merchantUidLow != null) {
                    dataLow.put("merchant_uid", merchantUidLow);
                }

                Map<String, Object> dataMiddle = new HashMap<>();
                dataMiddle.put("amount", middleprice);
                if (merchantUidMiddle != null) {
                    dataMiddle.put("merchant_uid", merchantUidMiddle);
                }

                Map<String, Object> dataHigh = new HashMap<>();
                dataHigh.put("amount", highprice);
                if (merchantUidHigh != null) {
                    dataHigh.put("merchant_uid", merchantUidHigh);
                }

                // API 호출 URL 구성
                String apiUrl = "https://api.iamport.kr/payments/prepare";

                // 각각의 가격에 대한 API 호출 실행
                Mono<ResponseEntity<String>> responseLow = null;
                Mono<ResponseEntity<String>> responseMiddle = null;
                Mono<ResponseEntity<String>> responseHigh = null;


                if (merchantUidLow != null) {
                    responseLow = webClient.post()
                            .uri(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .bodyValue(dataLow)
                            .retrieve()
                            .toEntity(String.class);
                }
                if (merchantUidMiddle != null) {
                    responseMiddle = webClient.post()
                            .uri(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .bodyValue(dataMiddle)
                            .retrieve()
                            .toEntity(String.class);
                }
                if (merchantUidHigh != null) {
                    responseHigh = webClient.post()
                            .uri(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .bodyValue(dataHigh)
                            .retrieve()
                            .toEntity(String.class);
                }

                // 응답 상태 코드 확인
                HttpStatusCode statusLow = null;
                HttpStatusCode statusMiddle = null;
                HttpStatusCode statusHigh = null;
                if (responseLow != null) {
                    statusLow = responseLow.block().getStatusCode();
                }
                if (responseMiddle != null) {
                    statusMiddle = responseMiddle.block().getStatusCode();
                }
                if (responseHigh != null) {
                    statusHigh = responseHigh.block().getStatusCode();
                }


                // 각각의 결과를 처리하는 로직 추가
                if (statusLow.is2xxSuccessful() && statusMiddle.is2xxSuccessful() && statusHigh.is2xxSuccessful()) {
                    // 모든 API 호출이 성공한 경우
                    PostBuyerInfoDTO postBuyerInfoDTO = new PostBuyerInfoDTO();
                    postBuyerInfoDTO.setLowprice(lowprice);
                    postBuyerInfoDTO.setMiddleprice(middleprice);
                    postBuyerInfoDTO.setHighprice(highprice);
                    postBuyerInfoDTO.setUserid(userid);
                    postBuyerInfoDTO.setUsername(username);
                    postBuyerInfoDTO.setInherentid(inherentid);
                    postBuyerInfoDTO.setMerchantUidLow(merchantUidLow);
                    postBuyerInfoDTO.setMerchantUidMiddle(merchantUidMiddle);
                    postBuyerInfoDTO.setMerchantUidHigh(merchantUidHigh);

                    postService.saveBuyerInfoPost(postBuyerInfoDTO);
                    return ResponseEntity.ok().build();
                } else {
                    // 하나 이상의 API 호출이 실패한 경우
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                throw new RuntimeException("Access token not found in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 멘토 특정 멀천트 아이디 조회
    @PostMapping("/post/selectmerchantid")
    public ResponseEntity<?> selectMerchantId(@RequestBody PostBuyerInfoDTO params,
                                              Model model) throws ApiException {
        Map<String, Object> obj = new HashMap<>();
        List<PostBuyerInfoDTO> posts = postService.findMerchantIdOnePost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("merchantuid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 결제 내역 삭제
    @DeleteMapping("/post/deletePaymentInfoAll")
    public ResponseEntity<?> deletePaymentAll(@RequestBody PostBuyerInfoDTO params) {
        try {
            postService.deleteBuyerInfoPost(params);
            return postService.deletePaidInfoPost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 멘토 타이틀 사진 조회
    @GetMapping("/writementortitleimage/{imageName:.+}")
    public ResponseEntity<byte[]> getWriteMentorTitleImage(@PathVariable("imageName") String mentortitleimage) throws IOException {
        Path imageWritementorPath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/image/" + mentortitleimage);
        byte[] imageBytes;
        try {
            imageBytes = Files.readAllBytes(imageWritementorPath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found", e);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    // 멘토 작성글 조회
    @GetMapping("/writementor")
    public ResponseEntity<?> mentorWrite(Model model) {
        Map<String, Object> obj = new HashMap<>();
        try {
            List<PostWritementorDTO> posts = postService.findMentorAllPost();
            model.addAttribute("posts", posts);
            obj.put("writementor", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 멘토 특정 작성글 조회
    @PostMapping("/writementor")
    public ResponseEntity<?> mentorWriteOne(@RequestBody PostWritementorDTO params, Model model) throws ApiException {
        Map<String, Object> obj = new HashMap<>();

        // Find posts for writementor
        List<PostWritementorDTO> writementorPosts = postService.findMentorOnePost(params.getId());
        model.addAttribute("writementor", writementorPosts);
        obj.put("writementor", writementorPosts);

        // Find posts for reviewlist
        List<PostWritementorDTO> reviewlistPosts = postService.findMentorReviewAppraisalOnePost(params.getId());
        model.addAttribute("reviewlist", reviewlistPosts);
        obj.put("reviewlist", reviewlistPosts);

        // Return response entity with obj and HTTP status OK
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }




    // 멘토 내가 쓴 글
    @PostMapping("/post/mywritementor")
    public ResponseEntity<?> myWriteMentor(@RequestBody PostWritementorDTO userid, Model model) {
        try {

            Map<String, Object> obj = new HashMap<>();

            //멘토 내가 쓴 글 조회
            List<PostWritementorDTO> writementorPosts = postService.findMentorPostByMyUserId(userid.getUserid());
            model.addAttribute("writementor", writementorPosts);
            obj.put("writementor", writementorPosts);

            //멘토 내가 쓴 글 별점 및 리뷰 조회
            List<PostWritementorDTO> reviewListPosts = postService.findMentorReviewAppraisalPostByMyUserId(userid.getUserid());
            model.addAttribute("reviewlist", reviewListPosts);
            obj.put("reviewlist", reviewListPosts);

            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 멘토 기존 게시글 수정
    @PostMapping("/post/updatewritementor")
    public ResponseEntity<?> updateWriteMentorPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.updateMentorPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 멘토 기존 게시글 삭제
    @DeleteMapping("/post/deletewritementor")
    public ResponseEntity<?> deleteWriteMentorPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.deleteMentorPost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 리뷰 및 별점 작성
    @PostMapping("/post/writefeedback")
    public ResponseEntity<?> saveWriteFeedBackPost(@RequestBody PostWritementorDTO params) {
        try {
            return ResponseEntity.ok(postService.saveWriteFeedBackPost(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    // 내가 쓴 리뷰 및 별점
    @PostMapping("/post/mywritefeedback")
    public ResponseEntity<?> myWriteFeedBack(@RequestBody PostWritementorDTO userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findFeedBackPostByMyUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostWritementorDTO> posts = postService.findFeedBackPostByMyUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("writefeedback", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 기존 리뷰 수정
    @PostMapping("/post/updatewritefeedback")
    public ResponseEntity<?> updateWriteFeedBackPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.updateFeedBackPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 기존 별점 및 리뷰 삭제
    @DeleteMapping("/post/deletewritefeedback")
    public ResponseEntity<?> deleteWriteFeedBackPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.deleteFeedBackPost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 유저 정보 등록
    @PostMapping("/sign")
    public ResponseEntity<?> saveUserPost(HttpServletRequest request, @RequestBody @Valid PostUserRequest params, Model model) throws ApiException {
        try {

            if ("USER".equals(params.getRoles())) {
                postService.saveUserPost(params);
                postService.saveUserRolesPost(params);
            } else if ("INSTRUCTOR".equals(params.getRoles())) {
                postService.saveUserPost(params);
                postService.updateInstructorUserPost(params);
            }
            Map<String, Object> obj = new HashMap<>();
            List<PostUserRequest> posts = postService.findUserInfoAllPost(params.getEmail());
            model.addAttribute("posts", posts);
            obj.put("userid", posts);
            return ResponseEntity.ok(obj);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 유저 모든 POST 요청 대한 권한얻기
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String userid = memberLoginRequestDto.getUserid();
        String password = memberLoginRequestDto.getPassword();
        try {
            TokenInfo tokenInfo = memberService.login(userid, password);
            PostUserRequest posts = postService.findUserRolesInfoAllPost(memberLoginRequestDto.getEmail());
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> tokenInfoMap = new HashMap<>();
            tokenInfoMap.put("grantType", tokenInfo.getGrantType());
            tokenInfoMap.put("accessToken", tokenInfo.getAccessToken());
            tokenInfoMap.put("refreshToken", tokenInfo.getRefreshToken());
            tokenInfoMap.put("roles", posts.getRoles());
            response.put("tokenInfo", tokenInfoMap);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // 엑세스토큰 재발급
    @PostMapping("/ribbonRefresh")
    public ResponseEntity<TokenInfo> refreshToken(@RequestBody ReissueToken params) {
        String refreshToken = params.getRefreshToken();
        String expireToken = params.getAccessToken();
        // RefreshToken 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().build();
        }
        // AccessToken 재발급
        Claims claims = jwtTokenProvider.parseClaims(expireToken);
        String username = claims.getSubject();
        List<String> roles = jwtTokenProvider.getRoles(expireToken);
        TokenInfo newTokenInfo = jwtTokenProvider.generateAccessToken(username, roles);

        return ResponseEntity.ok(newTokenInfo);
    }

    // 비밀키
    @Value("${myapp.secretKey}")
    private String secretKey;
    // CSRF 토큰 발급
    @PostMapping("/ribbon")
    public ResponseEntity<Map<String, String>> myPage(CsrfToken token, HttpServletRequest request, HttpServletResponse response,
                                                      @RequestBody PostSecretKey postSecretKey) {
        if (!postSecretKey.getSecrettoken().equals(secretKey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid secret key");
        }
        Map<String, String> tokens = new HashMap<>();
        tokens.put("csrfToken", token.getToken());;
        return ResponseEntity.ok(tokens);
    }


    // 유저 정보 수정
    @PostMapping("/post/updateuser")
    public ResponseEntity<?> updateUserPost(
            @RequestParam("sns") String sns,
            @RequestParam("nickname") @Size(min = 2, max = 10, message = "닉네임은 2~10자리여야 합니다.") @NotBlank(message = "닉네임은 필수 입력값입니다.") String nickname,
            @RequestParam("modifydate") @NotNull(message = "수정날짜는 필수입력값입니다.") String modifydate,
            @RequestParam("bestcategory") String bestcategory,
            @RequestParam("shortinfo") @Size(min = 2, max = 20, message = "한 줄 설명은 2~20자리여야 합니다.") String shortinfo,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("userid") @NotNull(message = "유저아이디는 필수입력값입니다.") Long userid) {

        try {

            PostUserUpdateRequest params = new PostUserUpdateRequest();
            if (file != null) {
                String profileimage = file.getOriginalFilename();
                String filepath = Paths.get(uploadPath, profileimage).toString();
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filepath);
                Files.write(path, bytes);
                params.setProfileimage(userip + profileimage);
            } else {
                params.setProfileimage("null");
            }
            params.setSns(sns);
            params.setNickname(nickname);
            params.setModifydate(modifydate);
            params.setBestcategory(bestcategory);
            params.setShortinfo(shortinfo);
            params.setUserid(userid);

            postService.updateUserPost(params);

            PostUserUpdateRequest posts = postService.findUserImagePost(params.getUserid());
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    // 유저 프로필 사진 조회
    @GetMapping("/userimage/{imageName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageName") String profileimage) throws IOException {
        Path imagePath = Paths.get("/Users/gim-yong-won/Desktop/ribbon/image/" + profileimage);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


        // 유저 정보 삭제
        @DeleteMapping("/post/deleteuser")
        public ResponseEntity<?> deleteUserPost(@RequestBody PostUserRequest params) {
            try {
                postService.deleteUserRolesPost(params);
                return new ResponseEntity<>(postService.deleteUserPost(params), HttpStatus.OK);
            } catch (ApiException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // 실시간 인기글
        @GetMapping("/realtimeup")
        public ResponseEntity<?> realTimeUp(Model model) {
            Map<String, Object> obj = new HashMap<>();
            try {
                List<PostResponse> posts = postService.findAllPost11();
                model.addAttribute("posts", posts);
                obj.put("bestwrite", posts);
                return new ResponseEntity<>(obj, HttpStatus.OK);
            } catch (ApiException e) {
                obj.put("error", e.getMessage());
                return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    // 좋아요 등록
        @PostMapping("/post/liked")
        public Integer saveLikedPost(@RequestBody PostLikedRequest params) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateLikedPost(params);
            postService.saveLikedPost(params);
            firebaseCloudMessageLikedService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            return ResponseEntity.ok().build().getStatusCode().value();

        }
        // 커뮤니티 좋아요 알림 조회
        @PostMapping("/post/likedalarm")
        public ResponseEntity<?> likedAlarm(@RequestBody PostLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostLikedRequest> posts = postService.findLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked1", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
        // 개인 좋아요 알림 조회
        @PostMapping("/post/individuallikedalarm")
        public ResponseEntity<?> usedLikedAlarm(@RequestBody PostIndividualLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostIndividualLikedRequest> posts = postService.findIndividualLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked2", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
        // 중고 좋아요 알림 조회
        @PostMapping("/post/usedlikedalarm")
        public ResponseEntity<?> usedLikedAlarm(@RequestBody PostUsedLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostUsedLikedRequest> posts = postService.findUsedLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked3", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 좋아요 삭제
        @DeleteMapping("/post/deleteliked")
        public ResponseEntity<?> deleteLikedPost(@RequestBody PostLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteLikedPost(params);
            return postService.deleteLikedPost(params);
        }

        // 개인 좋아요 등록
        @PostMapping("/post/individualliked")
        public Integer saveIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateIndividualLikedPost(params);
            postService.saveIndividualLikedPost(params);
            firebaseCloudMessageLikedService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            return ResponseEntity.ok().build().getStatusCode().value();

        }


        // 개인 좋아요 삭제
        @DeleteMapping("/post/deleteindividualliked")
        public ResponseEntity<?> deleteIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteIndividualLikedPost(params);
            return postService.deleteIndividualLikedPost(params);
        }

        // 중고 좋아요 등록
        @PostMapping("/post/usedliked")
        public Integer saveUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateUsedLikedPost(params);
            postService.saveUsedLikedPost(params);
            firebaseCloudMessageLikedService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            return ResponseEntity.ok().build().getStatusCode().value();

        }


        // 중고 좋아요 삭제
        @DeleteMapping("/post/deleteusedliked")
        public ResponseEntity<?> deleteUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteUsedLikedPost(params);
            return postService.deleteUsedLikedPost(params);
        }

    // 커뮤니티 댓글 알림 조회
    @PostMapping("/post/commentalarm")
    public ResponseEntity<?> commentsAlarm(@RequestBody PostCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsRequest> posts = postService.findCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment1", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 개인 댓글 알림 조회
    @PostMapping("/post/individualcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostIndiCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiCommentsRequest> posts = postService.findIndiCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment2", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 단체 댓글 알림 조회
    @PostMapping("/post/groupcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostGroupCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupCommentsRequest> posts = postService.findGroupCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment3", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 중고 댓글 알림 조회
    @PostMapping("/post/usedcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostUsedCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedCommentsRequest> posts = postService.findUsedCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment4", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

        // 댓글 조회
        @PostMapping("/post/commentsinfo")
        public ResponseEntity<?> commentsInfo(@RequestBody PostCommentsResponse inherentid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByInherentId(inherentid.getInherentid());
            Map<String, Object> obj = new HashMap<>();
            List<PostCommentsResponse> posts = postService.findPostByInherentId(inherentid.getInherentid());
            model.addAttribute("posts", posts);
            obj.put("comment", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 댓글 등록 및 아이디 전송
        @PostMapping("/post/comments")
        public ResponseEntity<?> saveComments(@RequestBody PostCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveCommentsPost(params);
            postService.updateCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCode();
            Map<String, Object> obj = new HashMap<>();
            List<PostCommentsIdResponse> posts = postService.findCommentsIdPost();
            model.addAttribute("posts", posts);
            obj.put("commentsid", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);

        }

        // 댓글 수정
        @PostMapping("/post/updatecomments")
        public ResponseEntity<?> updateCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateCommentsPost(params);
        }

        // 댓글 삭제
        @DeleteMapping("/post/deletecomments")
        public ResponseEntity<?> deleteCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteCommentsCountPost(params);
            return postService.deleteCommentsPost(params);
        }

        // 개인 댓글 조회
        @PostMapping("/post/indicommentsinfo")
        public ResponseEntity<?> indiCommentsInfo(@RequestBody PostIndiCommentsResponse inherentid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByIndiCommentsInherentId(inherentid.getInherentid());
            Map<String, Object> obj = new HashMap<>();
            List<PostIndiCommentsResponse> posts = postService.findPostByIndiCommentsInherentId(inherentid.getInherentid());
            model.addAttribute("posts", posts);
            obj.put("comment", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 개인 댓글 등록 및 아이디 전송
        @PostMapping("/post/indicomments")
        public ResponseEntity<?> saveIndiComments(@RequestBody PostIndiCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveIndiCommentsPost(params);
            postService.updateIndiCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCode();
            Map<String, Object> obj = new HashMap<>();
            List<PostIndiCommentsIdResponse> posts = postService.findIndiCommentsIdPost();
            model.addAttribute("posts", posts);
            obj.put("commentsid", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);

        }

        // 개인 댓글 수정
        @PostMapping("/post/updateindicomments")
        public ResponseEntity<?> updateIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateIndiCommentsPost(params);
        }

        // 개인 댓글 삭제
        @DeleteMapping("/post/deleteindicomments")
        public ResponseEntity<?> deleteIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteIndiCommentsCountPost(params);
            return postService.deleteIndiCommentsPost(params);
        }

        // 단체 댓글 조회
        @PostMapping("/post/groupcommentsinfo")
        public ResponseEntity<?> groupCommentsInfo(@RequestBody PostGroupCommentsResponse inherentid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByGroupCommentsInherentId(inherentid.getInherentid());
            Map<String, Object> obj = new HashMap<>();
            List<PostGroupCommentsResponse> posts = postService.findPostByGroupCommentsInherentId(inherentid.getInherentid());
            model.addAttribute("posts", posts);
            obj.put("comment", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 단체 댓글 등록 및 아이디 전송
        @PostMapping("/post/groupcomments")
        public ResponseEntity<?> saveGroupComments(@RequestBody PostGroupCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveGroupCommentsPost(params);
            postService.updateGroupCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCode();
            Map<String, Object> obj = new HashMap<>();
            List<PostGroupCommentsIdResponse> posts = postService.findGroupCommentsIdPost();
            model.addAttribute("posts", posts);
            obj.put("commentsid", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);

        }

        // 단체 댓글 수정
        @PostMapping("/post/updategroupcomments")
        public ResponseEntity<?> updateGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateGroupCommentsPost(params);
        }

        // 단체 댓글 삭제
        @DeleteMapping("/post/deletegroupcomments")
        public ResponseEntity<?> deleteGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteGroupCommentsCountPost(params);
            return postService.deleteGroupCommentsPost(params);
        }


        // 중고 댓글 조회
        @PostMapping("/post/usedcommentsinfo")
        public ResponseEntity<?> usedcommentsinfo(@RequestBody PostUsedCommentsResponse inherentid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
            Map<String, Object> obj = new HashMap<>();
            List<PostUsedCommentsResponse> posts = postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
            model.addAttribute("posts", posts);
            obj.put("comment", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 중고 댓글 등록 및 아이디 전송
        @PostMapping("/post/usedcomments")
        public ResponseEntity<?> saveUsedComments(@RequestBody PostUsedCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveUsedCommentsPost(params);
            postService.updateUsedCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCode();
            Map<String, Object> obj = new HashMap<>();
            List<PostUsedCommentsIdResponse> posts = postService.findUsedCommentsIdPost();
            model.addAttribute("posts", posts);
            obj.put("commentsid", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);

        }

        // 중고 댓글 수정
        @PostMapping("/post/updateusedcomments")
        public ResponseEntity<?> updateUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateUsedCommentsPost(params);
        }

        // 중고 댓글 삭제
        @DeleteMapping("/post/deleteusedcomments")
        public ResponseEntity<?> deleteUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteUsedCommentsCountPost(params);
            return postService.deleteUsedCommentsPost(params);
        }


        // 특정 유저 프로필 조회
        @PostMapping("/post/userinfo")
        public ResponseEntity<?> openPostView(@RequestBody UserInfoResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostById(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<UserInfoResponse> posts = postService.findPostById(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("userinfo", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 내가 쓴 글 커뮤니티
        @PostMapping("/post/myboardwrite")
        public ResponseEntity<?> myBoardWrite(@RequestBody PostMyBoardResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyBoardResponse> posts = postService.findPostByMyUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("myboard", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 쓴 글 단체
        @PostMapping("/post/mygroupwrite")
        public ResponseEntity<?> myBoardWrite(@RequestBody PostMyGroupResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyGroupUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyGroupResponse> posts = postService.findPostByMyGroupUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("groupwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 쓴 글 개인
        @PostMapping("/post/myindividualwrite")
        public ResponseEntity<?> myBoardWrite(@RequestBody PostMyIndiResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyIndividualUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyIndiResponse> posts = postService.findPostByMyIndividualUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("individualwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 쓴 글 중고
        @PostMapping("/post/myusedwrite")
        public ResponseEntity<?> myBoardWrite(@RequestBody PostMyUsedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyUsedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyUsedResponse> posts = postService.findPostByMyUsedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("usedwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 좋아요 누른 글 커뮤니티
        @PostMapping("/post/myboardliked")
        public ResponseEntity<?> myBoardLiked(@RequestBody PostMyLikedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyLikedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyLikedResponse> posts = postService.findPostByMyLikedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("myboard", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 좋아요 누른 글 개인
        @PostMapping("/post/myindividualliked")
        public ResponseEntity<?> myIndividualLiked(@RequestBody PostMyIndividualLikedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyIndividualLikedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyIndividualLikedResponse> posts = postService.findPostByMyIndividualLikedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("individualwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 좋아요 누른 글 중고
        @PostMapping("/post/myusedliked")
        public ResponseEntity<?> myUsedLiked(@RequestBody PostMyUsedLikedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyUsedLikedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyUsedLikedResponse> posts = postService.findPostByMyUsedLikedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("usedwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 채팅방 넣기
        @PostMapping("/post/chatroom")
        public ResponseEntity<?> saveChatRoomPost(@RequestBody PostChatRoomRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.saveChatRoomPost(params);

        }

        // 특정 채팅방 조회
        @PostMapping("/post/chatroominfo")
        public ResponseEntity<?> viewChatRoomPost(@RequestBody PostChatRoomResponse myid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyId(myid.getMyid());
            Map<String, Object> obj = new HashMap<>();
            List<PostChatRoomResponse> posts = postService.findPostByMyId(myid.getMyid());
            model.addAttribute("posts", posts);
            obj.put("chatroom", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 특정 채팅방 삭제
        @DeleteMapping("/post/deleteroom")
        public ResponseEntity<?> deleteChatRoomPost(@RequestBody PostChatRoomDeleteRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.deleteChatRoomPost(params);
        }

        // 채팅 넣기
        @PostMapping("/chat")
        public String pushChatPost(@RequestBody ResponseDTO responseDTO) throws IOException {
            firebaseCloudChatMessageService.sendMessageTo(
                    responseDTO.getToken(),
                    responseDTO.getNickname());
            return String.valueOf(ResponseEntity.ok().build().getStatusCode());

        }

}

