package com.project.ribbon.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.ribbon.customvaild.DigitLength;
import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.PostBuyerInfoDTO;
import com.project.ribbon.dto.PostRentalInfoDTO;
import com.project.ribbon.dto.ReissueToken;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;
import com.project.ribbon.filter.JwtAuthenticationFilter;
import com.project.ribbon.provide.JwtTokenProvider;
import com.project.ribbon.response.ApiException;
import com.project.ribbon.response.BadRequestException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
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


    @Qualifier("jwtTokenProvider")
    private final JwtTokenProvider jwtTokenProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ApplicationContext applicationContext;


    // 서버업로드용 서버 ip
    @Value("${myapp.cafe24Ip}")
    private String cafe24Ip;
    // 서버업로드용 이미지 파일 경로
    @Value("${myapp.cafe24Img}")
    private String cafe24Img;
    // 개발환경용 서버 ip
    @Value("${myapp.developIp}")
    private String developIp;
    // 개발환경용 이미지 파일 경로
    @Value("${myapp.developImg}")
    private String developImg;

    String userip = developIp+"userimage/";
    String boardip = developIp+"boardimage/";
    String groupip = developIp+"groupimage/";
    String usedip = developIp+"usedimage/";
    String mentorip = developIp+"writementortitleimage/";

    @Value("${file.upload.path}")
    private String uploadPath;

    // 커뮤니티 게시글 작성
    @PostMapping("/form/boardwrite")
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
        Path imageBoardPath = Paths.get(developImg + img);
        byte[] imageBytes = Files.readAllBytes(imageBoardPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 커뮤니티 게시글 조회
    @GetMapping("/board")
    public ResponseEntity<Map<String, Object>> boardWrite(Model model) {
        try {
            List<PostResponse> posts = postService.findAllPost();
            model.addAttribute("posts", posts);

            Map<String, Object> obj = new HashMap<>();
            obj.put("code", HttpStatus.OK.value());
            obj.put("data", posts);

            return ResponseEntity.ok(obj);
        } catch (BadRequestException e) {
            // Handle the BadRequestException here
            String errorMessage = "Bad request.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", errorMessage));
        } catch (Exception e) {
            // Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", errorMessage));
        }
    }

    // 커뮤니티 특정 게시글 조회
    @PostMapping("/board")
    public ResponseEntity<?> boardWriteOne(@RequestBody PostResponse params, Model model) throws ApiException {
        try {
        postService.updateBoardInquiryPost(params.getBoardid());
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findOnePost(params.getBoardid());
        model.addAttribute("posts", posts);
        obj.put("boardwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
// Handle the ApiException here
            String errorMessage = "An error occurred while processing the request.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
// Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 기존 게시글 수정
    @PutMapping("/form/board")
    public ResponseEntity<?> updatePost(@RequestBody PostRequest params) throws ApiException {
        try {
            return postService.updatePost(params);
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    // 기존 게시글 삭제
    @DeleteMapping("/form/board")
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
        try{
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupAllPost();
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    } catch (ApiException e) {
// Handle the ApiException here
        String errorMessage = "An error occurred while processing the request.";
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
// Handle any other exception types here
        String errorMessage = "An unexpected error occurred.";
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }


    // 단체 특정 작성글 조회
    @PostMapping("/group")
    public ResponseEntity<?> groupWriteOne(@RequestBody PostGroupResponse params, Model model) throws ApiException {
        try {
            postService.updateGroupInquiryPost(params.getGroupid());
            Map<String, Object> obj = new HashMap<>();
            List<PostGroupResponse> posts = postService.findGroupOnePost(params.getGroupid());
            model.addAttribute("posts", posts);
            obj.put("groupwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }catch (ApiException e) {
// Handle the ApiException here
                String errorMessage = "An error occurred while processing the request.";
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
// Handle any other exception types here
                String errorMessage = "An unexpected error occurred.";
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    // 단체 글작성
    @PostMapping("/form/groupwrite")
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
        Path imageGroupPath = Paths.get(developImg + titleimage);
        byte[] imageBytes = Files.readAllBytes(imageGroupPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 단체 게시글 수정
    @PutMapping("/form/group")
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
    @DeleteMapping("/form/group")
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
        try {
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiAllPost();
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
        }catch (ApiException e) {
// Handle the ApiException here
            String errorMessage = "An error occurred while processing the request.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
// Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 특정 작성글 조회
    @PostMapping("/individual")
    public ResponseEntity<?> indiWriteOne(@RequestBody PostIndiResponse params, Model model) throws ApiException {
        try {
        postService.updateIndiInquiryPost(params.getIndividualid());
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiOnePost(params.getIndividualid());
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
        }catch (ApiException e) {
// Handle the ApiException here
            String errorMessage = "An error occurred while processing the request.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
// Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 개인 글작성
    @PostMapping("/form/individualwrite")
    public ResponseEntity<?> saveIndiPost(@RequestBody @Valid PostIndiRequest params) throws ApiException {
        try {
            return new ResponseEntity<>(postService.saveIndiPost(params), HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 게시글 수정
    @PutMapping("/form/individual")
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
    @DeleteMapping("/form/individual")
    public ResponseEntity<?> deleteIndiPost(@RequestBody PostIndiRequest params) {
        try {
            postService.deleteIndiWriteCommentsPost(params);
            postService.deleteIndiWriteLikedPost(params);
            return postService.deleteIndiPost(params);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the post.");
        }
    }


    // 대여 작성글 조회
    @GetMapping("/used")
    public ResponseEntity<?> usedWrite(Model model) throws ApiException {
        try {
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedAllPost();
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
        }catch (ApiException e) {
// Handle the ApiException here
            String errorMessage = "An error occurred while processing the request.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
// Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 대여 특정 작성글 조회
    @PostMapping("/used")
    public ResponseEntity<?> usedWriteOne(@RequestBody PostUsedResponse params, Model model) throws ApiException {
        try {
        postService.updateUsedInquiryPost(params.getUsedid());
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedOnePost(params.getUsedid());
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
        }catch (ApiException e) {
// Handle the ApiException here
            String errorMessage = "An error occurred while processing the request.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
// Handle any other exception types here
            String errorMessage = "An unexpected error occurred.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 대여 글작성
    @PostMapping("/form/usedwrite")
    public ResponseEntity<?> saveUsedPost(
            @RequestParam("id") @Size(min = 2, max = 15, message = "카테고리는 2~15자리여야 합니다.") @NotNull(message = "카테고리는 필수 입력값입니다.") String id,
            @RequestParam("region") @NotBlank(message = "주소는 필수 입력값입니다.") String region,
            @RequestParam("detailregion") @NotBlank(message = "상세주소는 필수 입력값입니다.") String detailregion,
            @RequestParam("title") @Size(min = 2, max = 30, message = "제목은 2~30자리여야 합니다.") @NotBlank(message = "제목은 필수 입력 값입니다.") String title,
            @RequestParam("description") @NotBlank(message = "내용은 필수 입력 값입니다.") @Size(min = 2, max = 500, message = "내용은 2~500자리여야 합니다.") String description,
            @RequestParam("shortdescription") @NotBlank(message = "내용은 필수 입력 값입니다.") @Size(min = 2, max = 40, message = "내용은 2~40자리여야 합니다.") String shortdescription,
            @RequestParam(value = "usedimage1", required = false) MultipartFile file1,
            @RequestParam("userid") @NotNull(message = "유저아이디는 필수 입력값입니다.") Long userid,
            @RequestParam("writedate") @NotBlank(message = "작성날짜는 필수 입력값입니다.") String writedate,
            @RequestParam("nickname") @NotBlank(message = "닉네임은 필수 입력 값입니다.") String nickname,
            @RequestParam(value = "usedimage2", required = false) MultipartFile file2,
            @RequestParam(value = "usedimage3", required = false) MultipartFile file3,
            @RequestParam(value = "usedimage4", required = false) MultipartFile file4,
            @RequestParam(value = "usedimage5", required = false) MultipartFile file5,
            @RequestParam("storename") @NotBlank(message = "가게이름은 필수 입력값입니다.") String storename,
            @RequestParam("numpeople") @NotNull(message = "인원수는 필수 입력값입니다.") Integer numpeople,
            @RequestParam("opentime") @NotBlank(message = "영업시간은 필수 입력값입니다.") String opentime,
            @RequestParam("storetel") @NotBlank(message = "매장번호는 필수 입력값입니다.") String storetel,
            @RequestParam("parking") @NotBlank(message = "주차여부은 필수 입력값입니다.") String parking,
            @RequestParam("holiday") @NotBlank(message = "공휴일은 필수 입력값입니다.") String holiday,
            @RequestParam("price1")  Integer price1,
    @RequestParam(value = "price2",required = false)  Integer price2,
    @RequestParam(value = "price3",required = false)  Integer price3,
    @RequestParam(value = "price4",required = false)  Integer price4,
    @RequestParam(value = "price5",required = false)  Integer price5,
    @RequestParam(value = "price6",required = false)  Integer price6,
    @RequestParam(value = "price7",required = false)  Integer price7,
    @RequestParam(value = "price8",required = false)  Integer price8,
    @RequestParam(value = "price9",required = false)  Integer price9,
    @RequestParam(value = "price10",required = false)  Integer price10,
    @RequestParam("productname1")  String productname1,
    @RequestParam(value = "productname2",required = false)  String productname2,
    @RequestParam(value = "productname3",required = false)  String productname3,
    @RequestParam(value = "productname4",required = false)  String productname4,
    @RequestParam(value = "productname5",required = false)  String productname5,
    @RequestParam(value = "productname6",required = false)  String productname6,
    @RequestParam(value = "productname7",required = false)  String productname7,
    @RequestParam(value = "productname8",required = false)  String productname8,
    @RequestParam(value = "productname9",required = false)  String productname9,
    @RequestParam(value = "productname10",required = false)  String productname10)
            throws IOException {

        List<MultipartFile> files = Arrays.asList(file1, file2, file3,
                file4, file5);
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
        params.setDetailregion(detailregion);
        params.setTitle(title);
        params.setDescription(description);
        params.setShortdescription(shortdescription);
        params.setUserid(userid);
        params.setWritedate(writedate);
        params.setNickname(nickname);
        params.setStorename(storename);
        params.setNumpeople(numpeople);
        params.setOpentime(opentime);
        params.setStoretel(storetel);
        params.setParking(parking);
        params.setHoliday(holiday);
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
        params.setPrice1(price1);
        params.setPrice2(price2);
        params.setPrice3(price3);
        params.setPrice4(price4);
        params.setPrice5(price5);
        params.setPrice6(price6);
        params.setPrice7(price7);
        params.setPrice8(price8);
        params.setPrice9(price9);
        params.setPrice10(price10);
        params.setProductname1(productname1);
        params.setProductname2(productname2);
        params.setProductname3(productname3);
        params.setProductname4(productname4);
        params.setProductname5(productname5);
        params.setProductname6(productname6);
        params.setProductname7(productname7);
        params.setProductname8(productname8);
        params.setProductname9(productname9);
        params.setProductname10(productname10);
        postService.saveUsedPost(params);
        return new ResponseEntity<>(postService.saveRentalPost(params), HttpStatus.OK);

    }
    // 대여 포트원 주문번호, 금액 사전 등록, 디비 저장 (대여 금액 컨트롤러)
    @PostMapping("/form/pricebeforehandrentalinfo")
    public ResponseEntity<String> preparePaymentRental(@RequestParam("userid") Long userid,
                                                 @RequestParam("username") @Size(min = 2, max = 10) String username,
                                                 @RequestParam("price") @NotNull(message = "가격은 필수 입력값입니다.") @DigitLength(min = 3, max = 7, message = "가격은 4~7자리로 입력해주세요.") Integer price,
                                                 @RequestParam("merchantUid") String merchantUid,
                                                 @RequestParam("inherentid") Long inherentid,
                                                       @RequestParam("rentaltime") String rentaltime,
                                                       @RequestParam("productname") String  productname) throws IOException{
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
                dataLow.put("amount", price);
                if (merchantUid != null) {
                    dataLow.put("merchant_uid", merchantUid);
                }

                // API 호출 URL 구성
                String apiUrl = "https://api.iamport.kr/payments/prepare";

                // 각각의 가격에 대한 API 호출 실행
                Mono<ResponseEntity<String>> responseApi = null;

                if (merchantUid != null) {
                    responseApi = webClient.post()
                            .uri(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .bodyValue(dataLow)
                            .retrieve()
                            .toEntity(String.class);
                }

                // 응답 상태 코드 확인
                HttpStatusCode status = null;
                if (responseApi != null) {
                    status = Objects.requireNonNull(responseApi.block()).getStatusCode();
                }

                // 각각의 결과를 처리하는 로직 추가
                if (Objects.requireNonNull(status).is2xxSuccessful()) {
                    // 모든 API 호출이 성공한 경우
                    PostRentalInfoDTO postRentalInfoDTO = new PostRentalInfoDTO();
                    postRentalInfoDTO.setPrice(price);
                    postRentalInfoDTO.setUserid(userid);
                    postRentalInfoDTO.setUsername(username);
                    postRentalInfoDTO.setInherentid(inherentid);
                    postRentalInfoDTO.setMerchantUid(merchantUid);
                    postRentalInfoDTO.setProductname(productname);
                    postRentalInfoDTO.setRentaltime(rentaltime);

                    postService.saveRentalInfoPost(postRentalInfoDTO);
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

    // 대여 특정 멀천트 아이디 조회
    @PostMapping("/form/merchantidrental")
    public ResponseEntity<?> selectMerchantIdRental(@RequestBody PostRentalInfoDTO params,
                                              Model model) throws ApiException {
        Map<String, Object> obj = new HashMap<>();
        List<PostRentalInfoDTO> posts = postService.findMerchantIdRentalOnePost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("merchantuid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 대여 결제 내역 삭제
    @DeleteMapping("/form/paymentrentalinfoall")
    public ResponseEntity<?> deletePaymentRentalInfoAll(@RequestBody PostRentalInfoDTO params) {
        try {
            postService.deleteRentalInfoPost(params);
            return postService.deletePaidRentalInfoPost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // 대여 사진 조회
    @GetMapping("/usedimage/{imageName:.+}")
    public ResponseEntity<byte[]> getUsedImage(@PathVariable("imageName") String usedimage) throws IOException {
        Path imageUsedPath = Paths.get(developImg + usedimage);
        byte[] imageBytes = Files.readAllBytes(imageUsedPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    // 대여 게시글 수정
    @PutMapping("/form/used")
    public ResponseEntity<?> updateUsedPost(@RequestBody PostUsedRequest params) {
        try {
            return postService.updateUsedPost(params);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    // 대여 게시글 삭제
    @DeleteMapping("/form/used")
    public ResponseEntity<?> deleteUsedPost(@RequestBody PostUsedRequest params) {
        try {
            postService.deleteUsedWriteCommentsPost(params);
            postService.deleteUsedWriteLikedPost(params);
            postService.deleteRentalPriceAndProductNamePost(params);
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
    @PostMapping("/form/writementor")
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

    // 포트원 주문번호, 금액 사전 등록, 디비 저장 (멘토 금액 컨트롤러)
    @PostMapping("/form/pricebeforehandbuyerinfo")
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
                    statusLow = Objects.requireNonNull(responseLow.block()).getStatusCode();
                }
                if (responseMiddle != null) {
                    statusMiddle = Objects.requireNonNull(responseMiddle.block()).getStatusCode();
                }
                if (responseHigh != null) {
                    statusHigh = Objects.requireNonNull(responseHigh.block()).getStatusCode();
                }


                // 각각의 결과를 처리하는 로직 추가
                if (Objects.requireNonNull(statusLow).is2xxSuccessful() && Objects.requireNonNull(statusMiddle).is2xxSuccessful() && Objects.requireNonNull(statusHigh).is2xxSuccessful()) {
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
    @PostMapping("/form/mentormerchantid")
    public ResponseEntity<?> selectMerchantId(@RequestBody PostBuyerInfoDTO params,
                                              Model model) throws ApiException {
        Map<String, Object> obj = new HashMap<>();
        List<PostBuyerInfoDTO> posts = postService.findMerchantIdOnePost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("merchantuid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 결제 내역 삭제
    @DeleteMapping("/form/paymentinfoall")
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
        Path imageWritementorPath = Paths.get(developImg + mentortitleimage);
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
    @PostMapping("/form/mywritementor")
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
    @PutMapping("/form/writementor")
    public ResponseEntity<?> updateWriteMentorPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.updateMentorPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 멘토 기존 게시글 삭제
    @DeleteMapping("/form/writementor")
    public ResponseEntity<?> deleteWriteMentorPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.deleteMentorPost(params);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 리뷰 및 별점 작성
    @PostMapping("/form/writefeedback")
    public ResponseEntity<?> saveWriteFeedBackPost(@RequestBody PostWritementorDTO params) {
        try {
            return ResponseEntity.ok(postService.saveWriteFeedBackPost(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    // 내가 쓴 리뷰 및 별점
    @PostMapping("/form/mywritefeedback")
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
    @PutMapping("/form/writefeedback")
    public ResponseEntity<?> updateWriteFeedBackPost(@RequestBody PostWritementorDTO params) {
        try {
            return postService.updateFeedBackPost(params);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 기존 별점 및 리뷰 삭제
    @DeleteMapping("/form/writefeedback")
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
    public ResponseEntity<?> saveUserPost(@RequestBody @Valid PostUserRequest params, Model model) throws ApiException {
        try {
            if (params.getRoles().equals("FREEZINGUSER")) {
                throw new LockedException("계정이 잠김 처리되었습니다.");
            } else {
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
            }
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
            if (posts.getRoles().equals("FREEZINGUSER")) {
                throw new LockedException("계정이 잠김 처리되었습니다.");
            } else {
                Map<String, Object> response = new HashMap<>();
                Map<String, Object> tokenInfoMap = new HashMap<>();
                tokenInfoMap.put("grantType", tokenInfo.getGrantType());
                tokenInfoMap.put("accessToken", tokenInfo.getAccessToken());
                tokenInfoMap.put("refreshToken", tokenInfo.getRefreshToken());
                tokenInfoMap.put("roles", posts.getRoles());
                response.put("tokenInfo", tokenInfoMap);
                return ResponseEntity.ok(response);
            }
        } catch (BadCredentialsException e) {
            // Increase login attempts
            increaseLoginAttempts(userid);

            // Check if the account should be locked
            if (isAccountLocked(userid)) {
                postService.updateReportUserRolesPost(userid);
                throw new LockedException("계정이 잠김 처리되었습니다.");
            }

            // 클라이언트에게 보낼 응답 메시지 생성
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "invalid_grant");
            errorResponse.put("error_description", "잘못된 아이디 또는 비밀번호입니다.");
            // ResponseEntity 객체를 이용해 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    private static final int MAX_ATTEMPTS = 5; // Maximum login attempts allowed

    private Map<String, Integer> loginAttempts = new HashMap<>();

    // Increase login attempts and handle account lock logic
    private void increaseLoginAttempts(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            // Lock the account
            lockAccount(username);
        }
    }

    // Check if the account is locked
    private boolean isAccountLocked(String username) {
        return loginAttempts.containsKey(username) && loginAttempts.get(username) >= MAX_ATTEMPTS;
    }

    // Lock the account
    private void lockAccount(String username) {
        // Perform account locking logic here
        postService.updateReportUserRolesPost(username);
    }

    // 엑세스토큰 재발급
    @PostMapping("/ribbonrefresh")
    public ResponseEntity<TokenInfo> refreshToken(@RequestBody ReissueToken params, @RequestHeader HttpHeaders headers) {
        String refreshToken = params.getRefreshToken();
        String expireToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7);
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
    @PutMapping("/form/user")
    public ResponseEntity<?> updateUserPost(
            @RequestParam("sns") String sns,
            @RequestParam("nickname") @Size(min = 2, max = 10, message = "닉네임은 2~10자리여야 합니다.") @NotBlank(message = "닉네임은 필수 입력값입니다.") String nickname,
            @RequestParam("modifydate") @NotNull(message = "수정날짜는 필수입력값입니다.") String modifydate,
            @RequestParam("bestcategory") String bestcategory,
            @RequestParam("shortinfo") @Size(min = 2, max = 20, message = "한 줄 설명은 2~20자리여야 합니다.") String shortinfo,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("userid") @NotNull(message = "유저아이디는 필수입력값입니다.") Long userid,
            @RequestParam("account") @Size(min = 1, max = 34, message = "계좌번호는 최소1자, 최대34자로 입력해주세요.") String account,
            @RequestParam("bank") @Size(min = 1, max = 8, message = "최소1자, 최대8자로 입력해주세요.")String bank) {

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
            params.setAccount(account);
            params.setBank(bank);

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
        Path imagePath = Paths.get(developImg + profileimage);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


        // 유저 정보 삭제
        @DeleteMapping("/form/user")
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
        @PostMapping("/form/liked")
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
        @PostMapping("/form/likedalarm")
        public ResponseEntity<?> likedAlarm(@RequestBody PostLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostLikedRequest> posts = postService.findLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked1", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
        // 개인 좋아요 알림 조회
        @PostMapping("/form/individuallikedalarm")
        public ResponseEntity<?> usedLikedAlarm(@RequestBody PostIndividualLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostIndividualLikedRequest> posts = postService.findIndividualLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked2", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
        // 대여 좋아요 알림 조회
        @PostMapping("/form/usedlikedalarm")
        public ResponseEntity<?> usedLikedAlarm(@RequestBody PostUsedLikedRequest params, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostUsedLikedRequest> posts = postService.findUsedLikedAlarmPost(params.getUserid());
            model.addAttribute("posts", posts);
            obj.put("liked3", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 좋아요 삭제
        @DeleteMapping("/form/liked")
        public ResponseEntity<?> deleteLikedPost(@RequestBody PostLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteLikedPost(params);
            return postService.deleteLikedPost(params);
        }

        // 개인 좋아요 등록
        @PostMapping("/form/individualliked")
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
        @DeleteMapping("/form/individualliked")
        public ResponseEntity<?> deleteIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteIndividualLikedPost(params);
            return postService.deleteIndividualLikedPost(params);
        }

        // 대여 좋아요 등록
        @PostMapping("/form/usedliked")
        public Integer saveUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateUsedLikedPost(params);
            postService.saveUsedLikedPost(params);
            firebaseCloudMessageLikedService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            return ResponseEntity.ok().build().getStatusCode().value();

        }


        // 대여 좋아요 삭제
        @DeleteMapping("/form/usedliked")
        public ResponseEntity<?> deleteUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteUsedLikedPost(params);
            return postService.deleteUsedLikedPost(params);
        }

    // 커뮤니티 댓글 알림 조회
    @PostMapping("/form/commentalarm")
    public ResponseEntity<?> commentsAlarm(@RequestBody PostCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsRequest> posts = postService.findCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment1", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 개인 댓글 알림 조회
    @PostMapping("/form/individualcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostIndiCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiCommentsRequest> posts = postService.findIndiCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment2", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 단체 댓글 알림 조회
    @PostMapping("/form/groupcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostGroupCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupCommentsRequest> posts = postService.findGroupCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment3", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 대여 댓글 알림 조회
    @PostMapping("/form/usedcommentalarm")
    public ResponseEntity<?> usedLikedAlarm(@RequestBody PostUsedCommentsRequest params, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedCommentsRequest> posts = postService.findUsedCommentsAlarmPost(params.getUserid());
        model.addAttribute("posts", posts);
        obj.put("comment4", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

        // 댓글 조회
        @PostMapping("/form/commentsinfo")
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
        @PostMapping("/form/comments")
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
        @PutMapping("/form/comments")
        public ResponseEntity<?> updateCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateCommentsPost(params);
        }

        // 댓글 삭제
        @DeleteMapping("/form/comments")
        public ResponseEntity<?> deleteCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteCommentsCountPost(params);
            return postService.deleteCommentsPost(params);
        }

        // 개인 댓글 조회
        @PostMapping("/form/indicommentsinfo")
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
        @PostMapping("/form/indicomments")
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
        @PutMapping("/form/indicomments")
        public ResponseEntity<?> updateIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateIndiCommentsPost(params);
        }

        // 개인 댓글 삭제
        @DeleteMapping("/form/indicomments")
        public ResponseEntity<?> deleteIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteIndiCommentsCountPost(params);
            return postService.deleteIndiCommentsPost(params);
        }

        // 단체 댓글 조회
        @PostMapping("/form/groupcommentsinfo")
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
        @PostMapping("/form/groupcomments")
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
        @PutMapping("/form/groupcomments")
        public ResponseEntity<?> updateGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateGroupCommentsPost(params);
        }

        // 단체 댓글 삭제
        @DeleteMapping("/form/groupcomments")
        public ResponseEntity<?> deleteGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteGroupCommentsCountPost(params);
            return postService.deleteGroupCommentsPost(params);
        }


        // 대여 댓글 조회
        @PostMapping("/form/usedcommentsinfo")
        public ResponseEntity<?> usedcommentsinfo(@RequestBody PostUsedCommentsResponse inherentid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
            Map<String, Object> obj = new HashMap<>();
            List<PostUsedCommentsResponse> posts = postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
            model.addAttribute("posts", posts);
            obj.put("comment", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


        // 대여 댓글 등록 및 아이디 전송
        @PostMapping("/form/usedcomments")
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

        // 대여 댓글 수정
        @PutMapping("/form/usedcomments")
        public ResponseEntity<?> updateUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateUsedCommentsPost(params);
        }

        // 대여 댓글 삭제
        @DeleteMapping("/form/usedcomments")
        public ResponseEntity<?> deleteUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteUsedCommentsCountPost(params);
            return postService.deleteUsedCommentsPost(params);
        }


        // 특정 유저 프로필 조회
        @PostMapping("/form/userinfo")
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
        @PostMapping("/form/myboardwrite")
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
        @PostMapping("/form/mygroupwrite")
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
        @PostMapping("/form/myindividualwrite")
        public ResponseEntity<?> myBoardWrite(@RequestBody PostMyIndiResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyIndividualUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyIndiResponse> posts = postService.findPostByMyIndividualUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("individualwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 쓴 글 대여
        @PostMapping("/form/myusedwrite")
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
        @PostMapping("/form/myboardliked")
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
        @PostMapping("/form/myindividualliked")
        public ResponseEntity<?> myIndividualLiked(@RequestBody PostMyIndividualLikedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyIndividualLikedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyIndividualLikedResponse> posts = postService.findPostByMyIndividualLikedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("individualwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }

        // 내가 좋아요 누른 글 대여
        @PostMapping("/form/myusedliked")
        public ResponseEntity<?> myUsedLiked(@RequestBody PostMyUsedLikedResponse userid, Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyUsedLikedUserId(userid.getUserid());
            Map<String, Object> obj = new HashMap<>();
            List<PostMyUsedLikedResponse> posts = postService.findPostByMyUsedLikedUserId(userid.getUserid());
            model.addAttribute("posts", posts);
            obj.put("usedwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }


}

