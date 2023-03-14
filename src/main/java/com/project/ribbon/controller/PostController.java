package com.project.ribbon.controller;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;

import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
@ResponseBody
@Slf4j
@RestController
@Validated
public class PostController {

    private final PostService postService;
    private final FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;
    private final FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;
    private final FirebaseCloudChatMessageService firebaseCloudChatMessageService;

    private final MemberService memberService;

    // 외부 서버 ip : 112.148.33.214

    String userip = "http://192.168.0.5:8000/api/userimage/";
    String boardip = "http://192.168.0.5:8000/api/boardimage/";
    String groupip = "http://192.168.0.5:8000/api/groupimage/";
    String usedip = "http://192.168.0.5:8000/api/usedimage/";

    @Value("${file.upload.path}")
    private String uploadPath;

    // 커뮤니티 게시글 작성
    @PostMapping("/post/boardwrite")
    public ResponseEntity<?> savePost(
            @RequestParam("id") @NotNull(message = "카테고리 필수입력값입니다.") Integer id,
            @RequestParam("userid") @NotNull(message = "유저아이디 필수입력값입니다.") Long userId,
            @RequestParam("title") @NotBlank(message = "제목 필수입력값입니다.") @Size(min = 2, max = 30) String title,
            @RequestParam("description") @NotBlank(message = "내용 필수입력값입니다.") @Size(min = 2, max = 50) String description,
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
        System.out.println(params);
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
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updatePost(params);
    }

    // 기존 게시글 삭제
    @RequestMapping("/post/delete")
    public ResponseEntity<?> deletePost(@RequestBody PostRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.deleteBoardWriteCommentsPost(params);
        postService.deleteBoardWriteLikedPost(params);
        return postService.deletePost(params);
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
            @RequestParam(value = "image",required = false) MultipartFile file,
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
            params.setTitleimage(groupip+titleimage);
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
    public ResponseEntity<?> updateGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateGroupPost(params);
    }

    // 단체 게시글 삭제
    @RequestMapping("/post/deletegroup")
    public ResponseEntity<?> deleteGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.deleteGroupWriteCommentsPost(params);
        return postService.deleteGroupPost(params);
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
    public ResponseEntity<?> updateIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateIndiPost(params);
    }

    // 개인 게시글 삭제
    @RequestMapping("/post/deleteindividual")
    public ResponseEntity<?> deleteIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.deleteIndiWriteCommentsPost(params);
        postService.deleteIndiWriteLikedPost(params);
        return postService.deleteIndiPost(params);
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
            @RequestParam("description") @NotBlank(message = "내용은 필수 입력 값입니다.") String description,
            @RequestParam(value = "usedimage1", required = false) MultipartFile file1,
            @RequestParam("price") @NotNull(message = "가격은 필수 입력값입니다.") Integer price,
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
    public ResponseEntity<?> updateUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateUsedPost(params);
    }

    // 중고 게시글 삭제
    @RequestMapping("/post/deleteused")
    public ResponseEntity<?> deleteUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.deleteUsedWriteCommentsPost(params);
        postService.deleteUsedWriteLikedPost(params);
        return postService.deleteUsedPost(params);
    }


    // 유저 정보 등록
    @PostMapping("/sign")
    public ResponseEntity<?> saveUserPost(@RequestBody @Valid PostUserRequest params, Model model) throws ApiException {
        try {
            postService.saveUserPost(params);
            postService.saveUserRolesPost(params);

            Map<String, Object> obj = new HashMap<>();
            List<PostUserRequest> posts = postService.findUserInfoAllPost(params.getEmail());
            model.addAttribute("posts", posts);
            obj.put("userid", posts);
            System.out.println(posts);
            return ResponseEntity.ok(obj);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // 유저 모든 POST 요청 대한 권한얻기
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto,Model model) {
        String userid = memberLoginRequestDto.getUserid();
        String password = memberLoginRequestDto.getPassword();
        try {
            TokenInfo tokenInfo = memberService.login(userid, password);
            Map<String, Object> obj = new HashMap<>();
            List<PostUserRequest> posts = postService.findUserRolesInfoAllPost(memberLoginRequestDto.getEmail());
            model.addAttribute("posts", posts);
            obj.put("userroles", posts);
            obj.put("tokenInfo", tokenInfo);
            return ResponseEntity.ok(obj);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // 유저 정보 수정
    @PostMapping("/post/updateuser")
    public ResponseEntity<?> updateUserPost(
            @RequestParam("sns") String sns
            ,@RequestParam("nickname") @Size(min = 2, max = 10, message = "닉네임은 2~10자리여야 합니다.") @NotBlank(message = "닉네임은 필수 입력값입니다.") String nickname
            ,@RequestParam("modifydate") @NotNull(message = "수정날짜는 필수입력값입니다.") String modifydate
            ,@RequestParam("bestcategory") String bestcategory
            ,@RequestParam("shortinfo") @Size(min = 2, max = 20, message = "한 줄 설명은 2~20자리여야 합니다.") String shortinfo
            //,@RequestParam("youtube") String youtube
            ,@RequestParam(value = "image",required = false) MultipartFile file
            ,@RequestParam("userid") @NotNull(message = "유저아이디는 필수입력값입니다.") Long userid) throws IOException{
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
            System.out.println(posts);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 강사 정보 수정
    @PostMapping("/post/updateinstructoruser")
    public ResponseEntity<?> updateUserPost(@RequestBody PostInstructorUserUpdateRequest params) {
        try {
            postService.updateInstructorUserPost(params);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            // IOException 발생 시 500 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            // 그 외 예외 발생 시 400 에러와 함께 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
        @RequestMapping("/post/deleteuser")
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
        public ResponseEntity<?> realTimeUp(Model model) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            Map<String, Object> obj = new HashMap<>();
            List<PostResponse> posts = postService.findAllPost11();
            model.addAttribute("posts", posts);
            obj.put("bestwrite", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
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
            return ResponseEntity.ok().build().getStatusCodeValue();

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
        @RequestMapping("/post/deleteliked")
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
            return ResponseEntity.ok().build().getStatusCodeValue();

        }


        // 개인 좋아요 삭제
        @RequestMapping("/post/deleteindividualliked")
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
            return ResponseEntity.ok().build().getStatusCodeValue();

        }


        // 중고 좋아요 삭제
        @RequestMapping("/post/deleteusedliked")
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
        @RequestMapping("/post/commentsinfo")
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
        @RequestMapping(method = RequestMethod.POST, path = "/post/comments")
        public ResponseEntity<?> saveComments(@RequestBody PostCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveCommentsPost(params);
            postService.updateCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCodeValue();
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
        @RequestMapping("/post/deletecomments")
        public ResponseEntity<?> deleteCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteCommentsCountPost(params);
            return postService.deleteCommentsPost(params);
        }

        // 개인 댓글 조회
        @RequestMapping("/post/indicommentsinfo")
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
        @RequestMapping(method = RequestMethod.POST, path = "/post/indicomments")
        public ResponseEntity<?> saveIndiComments(@RequestBody PostIndiCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveIndiCommentsPost(params);
            postService.updateIndiCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCodeValue();
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
        @RequestMapping("/post/deleteindicomments")
        public ResponseEntity<?> deleteIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteIndiCommentsCountPost(params);
            return postService.deleteIndiCommentsPost(params);
        }

        // 단체 댓글 조회
        @RequestMapping("/post/groupcommentsinfo")
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
        @RequestMapping(method = RequestMethod.POST, path = "/post/groupcomments")
        public ResponseEntity<?> saveGroupComments(@RequestBody PostGroupCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveGroupCommentsPost(params);
            postService.updateGroupCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCodeValue();
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
        @RequestMapping("/post/deletegroupcomments")
        public ResponseEntity<?> deleteGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteGroupCommentsCountPost(params);
            return postService.deleteGroupCommentsPost(params);
        }


        // 중고 댓글 조회
        @RequestMapping("/post/usedcommentsinfo")
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
        @RequestMapping(method = RequestMethod.POST, path = "/post/usedcomments")
        public ResponseEntity<?> saveUsedComments(@RequestBody PostUsedCommentsRequest params, Model model) throws ApiException, IOException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.saveUsedCommentsPost(params);
            postService.updateUsedCommentsCountPost(params);
            firebaseCloudMessageCommentsService.sendMessageTo(
                    params.getToken(),
                    params.getNickname());
            ResponseEntity.ok().build().getStatusCodeValue();
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
        @RequestMapping("/post/deleteusedcomments")
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
        @RequestMapping("/post/deleteroom")
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
            return String.valueOf(ResponseEntity.ok().build().getStatusCodeValue());

        }

}

