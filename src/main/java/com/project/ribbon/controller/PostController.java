package com.project.ribbon.controller;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.TokenInfo;
import com.project.ribbon.enums.ExceptionEnum;

import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.api.gax.httpjson.HttpHeadersUtils.setHeader;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
@ResponseBody
@Slf4j
@RestController
public class PostController {

    private final PostService postService;
    private final FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;
    private final FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;
    private final FirebaseCloudChatMessageService firebaseCloudChatMessageService;

    private final MemberService memberService;


    String userip = "http://112.148.33.214:8000/api/userimage/";
    String boardip = "http://112.148.33.214:8000/api/boardimage/";
    String groupip = "http://112.148.33.214:8000/api/groupimage/";
    String usedip = "http://112.148.33.214:8000/api/usedimage/";

    @Value("${file.upload.path}")
    private String uploadPath;


    // 커뮤니티 게시글 작성
    @PostMapping("/post/boardwrite")
    public ResponseEntity<?> savePost(@RequestParam("id") Integer id
            ,@RequestParam("userid") Long userid,@RequestParam("title") String title
            ,@RequestParam("description") String description,@RequestParam(value = "image",required = false) MultipartFile file
            ,@RequestParam("writedate") String writedate
            ,@RequestParam("nickname") String nickname) throws IOException{
        if (file != null){
            String img = file.getOriginalFilename();
            String fileboardpath = Paths.get(uploadPath, img).toString();
            byte[] bytes = file.getBytes();
            Path boardimagepath = Paths.get(fileboardpath);
            Files.write(boardimagepath, bytes);
            PostRequest params = new PostRequest();
            params.setId(id);
            params.setUserid(userid);
            params.setTitle(title);
            params.setDescription(description);
            params.setImg(boardip+img);
            params.setWritedate(writedate);
            params.setNickname(nickname);

            return new ResponseEntity<>(postService.savePost(params), HttpStatus.OK);
        } else if (file == null) {
            PostRequest params = new PostRequest();
            params.setId(id);
            params.setUserid(userid);
            params.setTitle(title);
            params.setDescription(description);
            params.setImg(null);
            params.setWritedate(writedate);
            params.setNickname(nickname);

            return new ResponseEntity<>(postService.savePost(params), HttpStatus.OK);
        }
        return null;
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
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findOnePost(params.getBoardid());
        model.addAttribute("posts", posts);
        obj.put("boardwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    // 기존 게시글 수정
    @PostMapping("/post/update")
    public Long updatePost(@RequestBody PostRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updatePost(params);
    }

    // 기존 게시글 삭제
    @RequestMapping("/post/delete")
    public Long deletePost(@RequestBody PostRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
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
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupOnePost(params.getGroupid());
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 단체 글작성
    @PostMapping("/post/writegroup")
    public ResponseEntity<?> saveGroupPost(@RequestParam("id") Integer id
            ,@RequestParam("region") String region,@RequestParam("title") String title
            ,@RequestParam("line") String line
            , @RequestParam("description") String description
            ,@RequestParam("peoplenum") String peoplenum,@RequestParam("gender") String gender
            ,@RequestParam("minage") String minage,@RequestParam(value = "image",required = false) MultipartFile file
            ,@RequestParam("userid") Long userid,@RequestParam("maxage") String maxage
            ,@RequestParam("writedate") String writedate,@RequestParam("peoplenownum") String peoplenownum
            ,@RequestParam("nickname") String nickname,@RequestParam("once") String once) throws IOException{
        if (file != null){
            String titleimage = file.getOriginalFilename();
            String filegrouppath = Paths.get(uploadPath, titleimage).toString();
            byte[] bytes = file.getBytes();
            Path groupimagepath = Paths.get(filegrouppath);
            Files.write(groupimagepath, bytes);
            PostGroupRequest params = new PostGroupRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setLine(line);
            params.setDescription(description);
            params.setPeoplenum(peoplenum);
            params.setGender(gender);
            params.setMinage(minage);
            params.setTitleimage(groupip+titleimage);
            params.setUserid(userid);
            params.setMaxage(maxage);
            params.setWritedate(writedate);
            params.setPeoplenownum(peoplenownum);
            params.setNickname(nickname);
            params.setOnce(once);

            return new ResponseEntity<>(postService.saveGroupPost(params), HttpStatus.OK);
        } else if (file == null) {
            PostGroupRequest params = new PostGroupRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setLine(line);
            params.setDescription(description);
            params.setPeoplenum(peoplenum);
            params.setGender(gender);
            params.setMinage(minage);
            params.setTitleimage(null);
            params.setUserid(userid);
            params.setMaxage(maxage);
            params.setWritedate(writedate);
            params.setPeoplenownum(peoplenownum);
            params.setNickname(nickname);
            params.setOnce(once);

            return new ResponseEntity<>(postService.saveGroupPost(params), HttpStatus.OK);
        }
        return null;
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
    public Long updateGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateGroupPost(params);
    }

    // 단체 게시글 삭제
    @RequestMapping("/post/deletegroup")
    public Long deleteGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
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
            Long id = postService.saveIndiPost(params);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 게시글 수정
    @PostMapping("/post/updateindividual")
    public Long updateIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateIndiPost(params);
    }

    // 개인 게시글 삭제
    @RequestMapping("/post/deleteindividual")
    public Long deleteIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
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
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedOnePost(params.getUsedid());
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 중고 글작성
    @PostMapping("/post/writeused")
    public ResponseEntity<?> saveUsedPost(@RequestParam("id") Integer id
            ,@RequestParam("region") String region,@RequestParam("title") String title
            ,@RequestParam("description") String description
            ,@RequestParam(value = "usedimage1",required = false) MultipartFile file1,@RequestParam("price") Integer price
            ,@RequestParam("userid") Long userid,@RequestParam("writedate") String  writedate
            ,@RequestParam("nickname") String nickname,@RequestParam(value = "usedimage2",required = false) MultipartFile file2
            ,@RequestParam(value = "usedimage3",required = false) MultipartFile file3,@RequestParam(value = "usedimage4",required = false) MultipartFile file4
            ,@RequestParam(value = "usedimage5",required = false) MultipartFile file5) throws IOException{

        if (file1 !=null && file2 != null && file3 != null && file4 != null && file5 != null){
            String usedimage1 = file1.getOriginalFilename();
            String usedimage2 = file2.getOriginalFilename();
            String usedimage3 = file3.getOriginalFilename();
            String usedimage4 = file4.getOriginalFilename();
            String usedimage5 = file5.getOriginalFilename();
            String fileused1path = Paths.get(uploadPath, usedimage1).toString();
            byte[] bytes1 = file1.getBytes();
            Path groupimage1path = Paths.get(fileused1path);
            Files.write(groupimage1path, bytes1);
            String fileused2path = Paths.get(uploadPath, usedimage2).toString();
            byte[] bytes2 = file2.getBytes();
            Path groupimage2path = Paths.get(fileused2path);
            Files.write(groupimage2path, bytes2);
            String fileused3path = Paths.get(uploadPath, usedimage3).toString();
            byte[] bytes3 = file3.getBytes();
            Path groupimage3path = Paths.get(fileused3path);
            Files.write(groupimage3path, bytes3);
            String fileused4path = Paths.get(uploadPath, usedimage4).toString();
            byte[] bytes4 = file4.getBytes();
            Path groupimage4path = Paths.get(fileused4path);
            Files.write(groupimage4path, bytes4);
            String fileused5path = Paths.get(uploadPath, usedimage5).toString();
            byte[] bytes5 = file5.getBytes();
            Path groupimage5path = Paths.get(fileused5path);
            Files.write(groupimage5path, bytes5);
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(usedip+usedimage1);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(usedip+usedimage2);
            params.setUsedimage3(usedip+usedimage3);
            params.setUsedimage4(usedip+usedimage4);
            params.setUsedimage5(usedip+usedimage5);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        } else if (file1 ==null && file2 == null && file3 == null && file4 == null && file5 == null) {
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(null);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(null);
            params.setUsedimage3(null);
            params.setUsedimage4(null);
            params.setUsedimage5(null);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        } else if (file1 !=null && file2 == null && file3 == null && file4 == null && file5 == null) {
            String usedimage1 = file1.getOriginalFilename();
            String fileused1path = Paths.get(uploadPath, usedimage1).toString();
            byte[] bytes1 = file1.getBytes();
            Path groupimage1path = Paths.get(fileused1path);
            Files.write(groupimage1path, bytes1);
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(usedip+usedimage1);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(null);
            params.setUsedimage3(null);
            params.setUsedimage4(null);
            params.setUsedimage5(null);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        } else if (file1 !=null && file2 != null && file3 == null && file4 == null && file5 == null) {
            String usedimage1 = file1.getOriginalFilename();
            String usedimage2 = file2.getOriginalFilename();
            String fileused1path = Paths.get(uploadPath, usedimage1).toString();
            byte[] bytes1 = file1.getBytes();
            Path groupimage1path = Paths.get(fileused1path);
            Files.write(groupimage1path, bytes1);
            String fileused2path = Paths.get(uploadPath, usedimage2).toString();
            byte[] bytes2 = file2.getBytes();
            Path groupimage2path = Paths.get(fileused2path);
            Files.write(groupimage2path, bytes2);
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(usedip+usedimage1);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(usedip+usedimage2);
            params.setUsedimage3(null);
            params.setUsedimage4(null);
            params.setUsedimage5(null);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        } else if (file1 !=null && file2 != null && file3 != null && file4 == null && file5 == null) {
            String usedimage1 = file1.getOriginalFilename();
            String usedimage2 = file2.getOriginalFilename();
            String usedimage3 = file3.getOriginalFilename();
            String fileused1path = Paths.get(uploadPath, usedimage1).toString();
            byte[] bytes1 = file1.getBytes();
            Path groupimage1path = Paths.get(fileused1path);
            Files.write(groupimage1path, bytes1);
            String fileused2path = Paths.get(uploadPath, usedimage2).toString();
            byte[] bytes2 = file2.getBytes();
            Path groupimage2path = Paths.get(fileused2path);
            Files.write(groupimage2path, bytes2);
            String fileused3path = Paths.get(uploadPath, usedimage3).toString();
            byte[] bytes3 = file3.getBytes();
            Path groupimage3path = Paths.get(fileused3path);
            Files.write(groupimage3path, bytes3);
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(usedip+usedimage1);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(usedip+usedimage2);
            params.setUsedimage3(usedip+usedimage3);
            params.setUsedimage4(null);
            params.setUsedimage5(null);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        } else if (file1 !=null && file2 != null && file3 != null && file4 != null && file5 == null) {
            String usedimage1 = file1.getOriginalFilename();
            String usedimage2 = file2.getOriginalFilename();
            String usedimage3 = file3.getOriginalFilename();
            String usedimage4 = file4.getOriginalFilename();
            String fileused1path = Paths.get(uploadPath, usedimage1).toString();
            byte[] bytes1 = file1.getBytes();
            Path groupimage1path = Paths.get(fileused1path);
            Files.write(groupimage1path, bytes1);
            String fileused2path = Paths.get(uploadPath, usedimage2).toString();
            byte[] bytes2 = file2.getBytes();
            Path groupimage2path = Paths.get(fileused2path);
            Files.write(groupimage2path, bytes2);
            String fileused3path = Paths.get(uploadPath, usedimage3).toString();
            byte[] bytes3 = file3.getBytes();
            Path groupimage3path = Paths.get(fileused3path);
            Files.write(groupimage3path, bytes3);
            String fileused4path = Paths.get(uploadPath, usedimage4).toString();
            byte[] bytes4 = file4.getBytes();
            Path groupimage4path = Paths.get(fileused4path);
            Files.write(groupimage4path, bytes4);
            PostUsedRequest params = new PostUsedRequest();
            params.setId(id);
            params.setRegion(region);
            params.setTitle(title);
            params.setDescription(description);
            params.setUsedimage1(usedip+usedimage1);
            params.setPrice(price);
            params.setUserid(userid);
            params.setWritedate(writedate);
            params.setNickname(nickname);
            params.setUsedimage2(usedip+usedimage2);
            params.setUsedimage3(usedip+usedimage3);
            params.setUsedimage4(usedip+usedimage4);
            params.setUsedimage5(null);
            return new ResponseEntity<>(postService.saveUsedPost(params), HttpStatus.OK);
        }
        return null;
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
    public Long updateUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateUsedPost(params);
    }

    // 중고 게시글 삭제
    @RequestMapping("/post/deleteused")
    public Long deleteUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
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
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 유저 모든 POST 요청 대한 권한얻기
    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String userid = memberLoginRequestDto.getUserid();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(userid, password);
        return tokenInfo;
    }


    // 유저 정보 수정
    @PostMapping("/post/updateuser")
    public ResponseEntity<?> updateUserPost(
            @RequestParam("sns") String sns
            ,@RequestParam("nickname") String nickname,@RequestParam("modifydate") String modifydate
            ,@RequestParam("bestcategory") String bestcategory,@RequestParam("shortinfo") String shortinfo
            ,@RequestParam("youtube") String youtube, @RequestParam(value = "image",required = false) MultipartFile file
            ,@RequestParam("userid") Long userid) throws IOException{
        if (file!=null) {
            PostUserUpdateRequest params = new PostUserUpdateRequest();
            String profileimage = file.getOriginalFilename();
            String filepath = Paths.get(uploadPath, profileimage).toString();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filepath);
            Files.write(path, bytes);
            params.setSns(sns);
            params.setNickname(nickname);
            params.setModifydate(modifydate);
            params.setBestcategory(bestcategory);
            params.setShortinfo(shortinfo);
            params.setYoutube(youtube);
            params.setProfileimage(userip+profileimage);
            params.setUserid(userid);
            postService.updateUserPost(params);

            PostUserUpdateRequest posts = postService.findUserImagePost(params.getUserid());
            return new ResponseEntity<>(posts, HttpStatus.OK);

        } else if (file == null) {


            PostUserUpdateRequest params = new PostUserUpdateRequest();
            params.setSns(sns);
            params.setNickname(nickname);
            params.setModifydate(modifydate);
            params.setBestcategory(bestcategory);
            params.setShortinfo(shortinfo);
            params.setYoutube(youtube);
            params.setProfileimage(null);
            params.setUserid(userid);
            postService.updateUserPost(params);

            PostUserUpdateRequest posts = postService.findUserImagePost(params.getUserid());

            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        return null;
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


        // 좋아요 삭제
        @RequestMapping("/post/deleteliked")
        public Integer deleteLikedPost(@RequestBody PostLikedRequest params) throws ApiException {
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
        public Integer deleteIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException {
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
        public Integer deleteUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.updateDeleteUsedLikedPost(params);
            return postService.deleteUsedLikedPost(params);
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
        public Long updateCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateCommentsPost(params);
        }

        // 댓글 삭제
        @RequestMapping("/post/deletecomments")
        public Long deleteCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
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
        public Long updateIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateIndiCommentsPost(params);
        }

        // 개인 댓글 삭제
        @RequestMapping("/post/deleteindicomments")
        public Long deleteIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
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
        public Long updateGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateGroupCommentsPost(params);
        }

        // 단체 댓글 삭제
        @RequestMapping("/post/deletegroupcomments")
        public Long deleteGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
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
        public Long updateUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
            ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            return postService.updateUsedCommentsPost(params);
        }

        // 중고 댓글 삭제
        @RequestMapping("/post/deleteusedcomments")
        public Long deleteUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
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
        public Long saveChatRoomPost(@RequestBody PostChatRoomRequest params) throws ApiException {
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
        public String deleteChatRoomPost(@RequestBody PostChatRoomDeleteRequest params) throws ApiException {
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

